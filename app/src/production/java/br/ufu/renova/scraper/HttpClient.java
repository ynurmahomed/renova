package br.ufu.renova.scraper;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by yassin on 04/12/14.
 */
public class HttpClient implements IHttpClient {

    private String patronhost;
    private String sessionId;
    private String username;

    private List<Book> books;

    public HttpClient(String username, String password) throws IOException, LoginException {

        this.username = username;
        patronhost = Scraper.scrapePatronhost();
        sessionId = Scraper.login(patronhost, username, password);

        try {
            books = Scraper.scrapeBooks(sessionId);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SessionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public void renew(Book b) throws RenewDateException, BookReservedException, IOException {
        Scraper.renew(patronhost, sessionId, username, b);
    }
}
