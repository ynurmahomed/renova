package br.ufu.renova.scraper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by yassin on 10/19/16.
 */
public interface IHttpClient extends Serializable {

    List<Book> getBooks();

    void renew(Book b) throws IOException, RenewException;
}
