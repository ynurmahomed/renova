package br.ufu.renova.scraper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by yassin on 10/19/16.
 */
public interface IHttpClient extends Serializable {

    void login(String username, String password) throws IOException, LoginException, ScrapeException;

    List<Book> getBooks() throws IOException, SessionExpiredException, ScrapeException;

    void renew(Book b) throws IOException, RenewException;
}
