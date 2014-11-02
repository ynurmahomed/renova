package com.renovapp.app.scraper;


import android.net.Uri;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yassin on 27/10/14.
 */
public class HttpClient implements Serializable {
    private final String URL = "http://www.acervobiblioteca.ufu.br:8000/cgi-bin/gw/chameleon";

    private String patronhost;
    private String sessionId;
    private String username;

    private List<Book> books;

    public HttpClient(String username, String password) throws IOException, LoginException {
        this.username = username;
        scrapePatronhost();
        login(password);
    }

    private void login(String password) throws IOException, LoginException {

        try {
            Document doc = Jsoup.connect(buildLoginURL(username, password)).get();

            Element loginError = doc.select("body > table:nth-child(7) > tbody > tr:nth-child(1) > td:nth-child(3) > table > tbody > tr > td > h2").first();

            if (loginError != null)
                throw new LoginException();

            scrapeSessionId(doc);
            scrapeBooks(doc);

            Log.d("HttpClient", "logged in user " + username);

        } catch (ParseException e) {
            Log.d("HttpClient", e.toString());
            e.printStackTrace();
            throw new LoginException();
        }
    }

    public void renew(Book book) throws IOException, BookReservedException, RenewDateException {
        Document doc = Jsoup.connect(buildRenewURL(book)).get();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String renewMessage = doc.select("table.outertable > tbody > tr:nth-child(7) > td").first().text();

        try {

            Date expirationDate = dateFormat.parse(renewMessage);
            book.setExpiration(expirationDate);

        } catch (ParseException e) {
            //
            // duas situações possíveis:
            // *   renovação cancelada por causa da data renovação
            // *   renovação cancelada porque o livro foi solicitado
            //
            Log.d("HttpClient", "book " + book.getBarcode() + " renew failed: " + renewMessage);
            e.printStackTrace();

            if (isRenewDateInvalid(renewMessage)) {
                throw new RenewDateException();
            } else {
                throw new BookReservedException();
            }
        }

    }

    private String buildLoginURL(String username, String password) {
        return Uri.parse(this.URL).buildUpon()
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

    private String buildRenewURL(Book book) {
        return Uri.parse(this.URL).buildUpon()
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

    private void scrapePatronhost() throws IOException {
        Document doc = Jsoup.connect(this.URL).get();
        patronhost = doc.select("input[name=patronhost]").first().val();
        Log.d("HttpClient", "patronhost is " + patronhost);
    }

    private void scrapeSessionId(Document doc) {
        Element refreshMeta = doc.select("meta[http-equiv=Refresh]").first();
        String[] parts = refreshMeta.attr("content").split("url=");

        Uri theUrl = Uri.parse(parts[1]);

        sessionId = theUrl.getQueryParameter("sessionid");

        Log.d("HttpClient", "refreshMeta is " + theUrl.toString());
        Log.d("HttpClient", "sessionId is " + sessionId);
    }

    private void scrapeBooks(Document doc) throws ParseException {
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

        books = new ArrayList<Book>(numBooks);

        for (int i = 0; i < numBooks; i++) {
            // Divide titulos em titulos e autores
            String[] parts = titlesAndAuthors.get(i).text().split("/");
            titles[i] = parts[0];
            authors[i] = parts[1];

            // Parse das datas
            expirations[i] = dateFormat.parse(expirationDates.get(i).text());

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

    }

    private boolean isRenewDateInvalid(String renewMessage) {
        return renewMessage.contains("data de renovação");
    }

    public List<Book> getBooks() {
        return books;
    }

}
