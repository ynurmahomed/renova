package br.ufu.renova.scraper;


import android.net.Uri;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yassin on 27/10/14.
 */
public class Scraper {
    private static final String URL = "http://www.acervobiblioteca.ufu.br:8000/cgi-bin/gw/chameleon";
    /**
     * Regex para caracteres que devem ser removidos da string dos autores.
     */
    private static final String AUTHORS_INVALID_CHAR_REGEX = "[.\\[\\]-]";

    public static String login(String patronhost, String username, String password) throws IOException, LoginException {

        Document doc = Jsoup.connect(buildLoginURL(patronhost, username, password)).get();

        Element loginError = doc.select("body > table:nth-child(7) > tbody > tr:nth-child(1) > td:nth-child(3) > table > tbody > tr > td > h2")
                                .first();

        if (loginError != null)
            throw new LoginException();

        Log.d("Scraper", "logged in user " + username);

        return scrapeSessionId(doc);
    }

    public static void renew(String patronhost, String sessionId, String username, Book book) throws IOException, BookReservedException, RenewDateException {

        Log.d("Scraper", "renewing book " + book.getBarcode() + "...");

        if (book.getState().isErrorState) {
            Log.d("Scraper", "renew cancelled: " + book.getState().msg);
            return;
        }


        Document doc = Jsoup.connect(buildRenewURL(patronhost, sessionId, username, book)).get();

        Elements tableLines = doc.select("table.outertable > tbody > tr");
        String renewMessage = tableLines.last().text();

        //
        // três situações possíveis:
        // *   renovação cancelada por causa da data renovação
        // *   TODO: renovação cancelada porque o livro foi solicitado
        // *   TODO: renovação cancelada por causa do limite de renovações
        //
        if (isRenewDateInvalid(renewMessage)) {
            book.setState(Book.State.INVALID_RENEW_DATE);
            throw new RenewDateException();
        }

        try {

            Element header = null;
            Element value = null;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date expirationDate;
            for (Element line: tableLines) {
                header = line.select("th").first();
                value = line.select("td").first();
                if (header != null && header.text().contains("Data Devolução") && value != null) {
                    expirationDate = dateFormat.parse(value.text());
                    book.setExpiration(expirationDate);
                    book.setState(Book.State.RENEWED);
                }
            }

        } catch (ParseException e) {
            Log.d("Scraper", "renew failed: " + renewMessage);
            e.printStackTrace();
        }

    }

    private static String buildBooksURL(String sessionId) {
        return Uri.parse(URL).buildUpon()
                .appendQueryParameter("sessionid", sessionId)
                .appendQueryParameter("function", "PATRONSUMMARY")
                .build()
                .toString();
    }

    private static String buildLoginURL(String patronhost, String username, String password) {
        return Uri.parse(URL).buildUpon()
                .appendQueryParameter("function", "PATRONATTEMPT")
                .appendQueryParameter("search", "PATRON")
                .appendQueryParameter("lng", "pt")
                .appendQueryParameter("login", "1")
                .appendQueryParameter("conf", "./chameleon.conf")
                .appendQueryParameter("u1", "12")
                .appendQueryParameter("SourceScreen", "PATRONLOGIN")
                .appendQueryParameter("patronid", username)
                .appendQueryParameter("patronpassword", password)
                .appendQueryParameter("patronhost", patronhost)
                .build()
                .toString();
    }

    private static String buildRenewURL(String patronhost, String sessionId, String username, Book book) {
        return Uri.parse(URL).buildUpon()
                .appendQueryParameter("function", "RENEWAL")
                .appendQueryParameter("search", "PATRON")
                .appendQueryParameter("sessionid", sessionId)
                .appendQueryParameter("skin", "novo")
                .appendQueryParameter("lng", "pt")
                .appendQueryParameter("inst", "consortium")
                .appendQueryParameter("conf", "./chameleon.conf")
                .appendQueryParameter("sourcescreen", "PATRONSUMMARY")
                .appendQueryParameter("pos", "1")
                .appendQueryParameter("patronid", username)
                .appendQueryParameter("patronhost", patronhost)
                .appendQueryParameter("host", patronhost)
                .appendQueryParameter("itembarcode", book.getBarcode())
                .build()
                .toString();
    }

    public static String scrapePatronhost() throws IOException {
        Document doc = Jsoup.connect(URL).get();
        String patronhost = doc.select("input[name=patronhost]")
                               .first()
                               .val();
        Log.d("Scraper", "patronhost is " + patronhost);
        return patronhost;
    }

    private static String scrapeSessionId(Document doc) {
        Element refreshMeta = doc.select("meta[http-equiv=Refresh]").first();
        String[] parts = refreshMeta.attr("content").split("url=");

        Uri theUrl = Uri.parse(parts[1]);

        String sessionId = theUrl.getQueryParameter("sessionid");

        Log.d("Scraper", "refreshMeta is " + theUrl.toString());
        Log.d("Scraper", "sessionId is " + sessionId);

        return sessionId;
    }

    public static List<Book> scrapeBooks(String sessionId) throws ParseException, IOException, SessionException {

        Document doc = Jsoup.connect(buildBooksURL(sessionId)).get();

        if (isSessionExpired(doc)) {
            throw new SessionException();
        }

        Elements bookTableRows = doc.select("table.patSumCheckedOutTable > tbody > tr:not(:first-child)");

        Elements expirationDates = bookTableRows.select("td:nth-child(1)");
        Elements titlesAndAuthors = bookTableRows.select("td:nth-child(2)");
        Elements callNumbers = bookTableRows.select("td:nth-child(3)");
        Elements notes = bookTableRows.select("td:nth-child(4)");
        Elements statuses = bookTableRows.select("td:nth-child(5)");
        Elements barcodes = bookTableRows.select("td:nth-child(6) > input");

        int numBooks = bookTableRows.size();

        String[] titles = new String[numBooks];
        String[] authors = new String[numBooks];
        Date[] expirations = new Date[numBooks];

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        ArrayList<Book> books = new ArrayList<Book>(numBooks);

        for (int i = 0; i < numBooks; i++) {
            // Divide titulos em titulos e autores
            String[] parts = titlesAndAuthors.get(i)
                                             .text()
                                             .split("/");
            titles[i] = parts[0];

            if (parts.length > 1) {
                authors[i] = parts[1]
                        .replaceAll(AUTHORS_INVALID_CHAR_REGEX, "")
                        .trim();
            } else {
                authors[i] = "";
                titles[i] = titles[i].trim()
                                     .replaceAll("\\.$", "");
            }

            // Parse das datas
            expirations[i] = dateFormat.parse(expirationDates.get(i)
                                                             .text());

            // Cria os livros
            Book b = new Book();
            b.setExpiration(expirations[i]);
            b.setTitle(titles[i]);
            b.setAuthors(authors[i].trim());
            b.setCallNumber(callNumbers.get(i).text());
            b.setNote(notes.get(i).text());
            b.setStatus(statuses.get(i).text());
            b.setBarcode(barcodes.get(i).val());
            books.add(b);

        }

        return books;

    }

    private static boolean isSessionExpired(Document doc) {
        return !doc.select("form[name='patonsearch']").isEmpty();
    }

    private static boolean isRenewDateInvalid(String renewMessage) {
        return renewMessage.contains("data de renovação");
    }

}
