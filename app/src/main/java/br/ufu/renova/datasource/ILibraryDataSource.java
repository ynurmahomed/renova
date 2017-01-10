package br.ufu.renova.datasource;

import br.ufu.renova.model.Book;
import br.ufu.renova.model.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yassin on 10/19/16.
 */
public interface ILibraryDataSource extends Serializable {

    interface LoginCallback {
        void onComplete(User user);
        void onError(Exception e);
    }

    interface GetBooksCallback {
        void  onComplete(List<Book> books);
        void  onError(Exception e);
    }

    interface RenewCallback {
        void onComplete(Book book);
        void onError(Exception e);
    }

    void login(String username, String password, LoginCallback callback);

    void getBooks(GetBooksCallback callback);

    void renew(Book b, RenewCallback callback);
}
