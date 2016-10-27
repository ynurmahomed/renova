package br.ufu.renova.scraper;

import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by yassin on 04/12/14.
 */
public class HttpClient implements IHttpClient {

    private String patronhost;
    private String sessionId;
    private String username;

    private List<Book> books;

    public HttpClient(String username, String password) throws ScrapeException, LoginException, IOException {

        this.username = username;
        patronhost = Scraper.scrapePatronhost();
        sessionId = Scraper.login(patronhost, username, password);

        try {
            books = Scraper.scrapeBooks(sessionId);
        } catch (ScrapeException | SessionExpiredException e) {
            Log.e(this.getClass().getName(), "", e);
        }
    }


    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public void renew(Book b) throws RenewException, IOException {
        Scraper.renew(patronhost, sessionId, username, b);
    }
}
