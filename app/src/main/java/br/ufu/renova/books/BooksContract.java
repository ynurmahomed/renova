package br.ufu.renova.books;

import br.ufu.renova.BasePresenter;
import br.ufu.renova.BaseView;
import br.ufu.renova.model.Book;

import java.util.List;

/**
 * Created by yassin on 11/8/16.
 */
public interface BooksContract {

    interface View extends BaseView<Presenter> {

        void showBooksList(List<Book> books);

        void showEmptyView();

        void showProgress();

        void showSessionExpiredToast();

        void showScrapeErrorToast();

        void showNoConnectionToast();

        void showBookErrorToast(Book b);
    }

    interface Presenter extends BasePresenter {

        void onBookClick(Book b);

        void onBookErrorIconClick(Book b);

        void onReloadClick();
    }
}
