package br.ufu.renova.scraper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yassin on 04/12/14.
 */
public class HttpClient implements IHttpClient {

    private Book[] books;

    public HttpClient(String username, String password) throws IOException, LoginException {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

        Book b1 = new Book();
        b1.setTitle("Effective Java");
        b1.setAuthors("Joshua Bloch");
        b1.setBarcode("123456789");
        b1.setExpiration(tomorrow.getTime());

        Book b2 = new Book();
        b2.setTitle("Machine Learning");
        b2.setAuthors("Tom M. Mitchell");
        b2.setBarcode("987654321");
        b2.setExpiration(tomorrow.getTime());

        books = new Book[]{b1, b2};
    }

    @Override
    public List<Book> getBooks() {
        return Arrays.asList(books);
    }

    @Override
    public void renew(Book b) throws RenewDateException, BookReservedException, IOException {
        Calendar nextWeek = Calendar.getInstance();
        nextWeek.setTime(b.getExpiration());
        nextWeek.add(Calendar.DAY_OF_MONTH, 7);
        b.setExpiration(nextWeek.getTime());
        b.setState(Book.State.RENEWED);
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
