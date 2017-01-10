package br.ufu.renova.datasource;

import android.support.test.espresso.idling.CountingIdlingResource;
import br.ufu.renova.model.Book;
import br.ufu.renova.model.User;

import java.util.List;

/**
 * Embrulha métodos de {@link ILibraryDataSource} de forma que os testes de tela esperem
 * que as operações assincronas terminem.
 *
 * Created by yassin on 11/22/16.
 */
public class DecoratedUFULibraryDataSourceMock implements ILibraryDataSource {

    private final ILibraryDataSource dataSource;

    private final CountingIdlingResource idlingResource;

    public DecoratedUFULibraryDataSourceMock(ILibraryDataSource dataSource, CountingIdlingResource idlingResource) {
        this.dataSource = dataSource;
        this.idlingResource = idlingResource;
    }


    @Override
    public void login(String username, String password, final LoginCallback callback) {
        idlingResource.increment();
        dataSource.login(username, password, new LoginCallback() {
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
        dataSource.getBooks(new GetBooksCallback() {
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
        dataSource.renew(b, new RenewCallback() {
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
