package br.ufu.renova.scraper;

import android.os.Handler;
import br.ufu.renova.model.Book;
import br.ufu.renova.model.User;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by yassin on 04/12/14.
 */
public class UFUHttpClient implements IHttpClient {

    private static UFUHttpClient INSTANCE;

    private final long DELAY = 1000L;

    private Book[] books;

    public static UFUHttpClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UFUHttpClient();
        }
        return INSTANCE;
    }

    private UFUHttpClient() {
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

        Book b3 = new Book();
        b3.setTitle("Introduction to Algorithms");
        b3.setAuthors("Charles E. Leiserson, Thomas H. Cormen, Clifford Stein, Ronald Rivest");
        b3.setBarcode("123456789");
        b3.setExpiration(tomorrow.getTime());

        Book b4 = new Book();
        b4.setTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        b4.setAuthors("Erich Gamma, Ralph Johnson, Richard Helm, John Vlissides");
        b4.setBarcode("987654321");
        b4.setExpiration(tomorrow.getTime());

        books = new Book[]{b1, b2, b3, b4};
    }

    @Override
    public void login(final String login, final String password, final LoginCallback callback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(new User(login, password));
            }
        }, DELAY);
    }

    @Override
    public void getBooks(final GetBooksCallback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callback.onComplete(Arrays.asList(books));
                    }
                }, DELAY);
            }
        }).start();
    }

    @Override
    public void renew(final Book b, final RenewCallback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Calendar nextWeek = Calendar.getInstance();
                        nextWeek.setTime(b.getExpiration());
                        nextWeek.add(Calendar.DAY_OF_MONTH, 7);
                        b.setExpiration(nextWeek.getTime());
                        b.setState(Book.State.RENEWED);
                        callback.onComplete(b);
                    }
                }, DELAY);
            }
        }).start();
    }
}
