package br.ufu.renova.scraper;

import java.io.IOException;
import java.util.List;

/**
 * Created by yassin on 04/12/14.
 */
public class UFUHttpClient implements IHttpClient {

    private static UFUHttpClient INSTANCE;

    private String patronhost;

    private String sessionId;

    private String username;

    public static UFUHttpClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UFUHttpClient();
        }
        return INSTANCE;
    }

    private UFUHttpClient() {
    }

    @Override
    public void login(String username, String password) throws IOException, LoginException, ScrapeException {
        this.username = username;
        patronhost = Scraper.scrapePatronhost();
        sessionId = Scraper.login(patronhost, username, password);
    }

    @Override
    public List<Book> getBooks() throws IOException, SessionExpiredException, ScrapeException {
        return Scraper.scrapeBooks(sessionId);
    }

    @Override
    public void renew(Book b) throws RenewException, IOException {
        Scraper.renew(patronhost, sessionId, username, b);
    }
}
