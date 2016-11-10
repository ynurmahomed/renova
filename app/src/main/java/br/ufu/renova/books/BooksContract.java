package br.ufu.renova.books;

import br.ufu.renova.BasePresenter;
import br.ufu.renova.BaseView;
import br.ufu.renova.scraper.Book;

import java.util.List;

/**
 * Created by yassin on 11/8/16.
 */
public interface BooksContract {

    interface View extends BaseView<Presenter> {
        void showBooksList(List<Book> books);
    }

    interface Presenter extends BasePresenter {
        void renew(Book b);
    }
}
