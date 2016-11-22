package br.ufu.renova.scraper;

import android.support.test.espresso.idling.CountingIdlingResource;
import br.ufu.renova.model.Book;
import br.ufu.renova.model.User;

import java.util.List;

/**
 * Embrulha métodos de {@link IHttpClient} de forma que os testes de tela esperem
 * que as operações assincronas terminem.
 *
 * Created by yassin on 11/22/16.
 */
public class DecoratedUFUHttpClientMock implements IHttpClient {

    private final IHttpClient httpClient;

    private final CountingIdlingResource idlingResource;

    public DecoratedUFUHttpClientMock(IHttpClient httpClient, CountingIdlingResource idlingResource) {
        this.httpClient = httpClient;
        this.idlingResource = idlingResource;
    }


    @Override
    public void login(String username, String password, final LoginCallback callback) {
        idlingResource.increment();
        httpClient.login(username, password, new LoginCallback() {
            @Override
            public void onComplete(User user) {
                callback.onComplete(user);
                idlingResource.decrement();
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
                idlingResource.decrement();
            }
        });
    }

    @Override
    public void getBooks(final GetBooksCallback callback) {
        idlingResource.increment();
        httpClient.getBooks(new GetBooksCallback() {
            @Override
            public void onComplete(List<Book> books) {
                callback.onComplete(books);
                idlingResource.decrement();
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
                idlingResource.decrement();
            }
        });
    }

    @Override
    public void renew(Book b, final RenewCallback callback) {
        idlingResource.increment();
        httpClient.renew(b, new RenewCallback() {
            @Override
            public void onComplete(Book book) {
                callback.onComplete(book);
                idlingResource.decrement();
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
                idlingResource.decrement();
            }
        });
    }
}
