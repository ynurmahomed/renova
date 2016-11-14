package br.ufu.renova.books;

import android.util.Log;
import br.ufu.renova.model.Book;
import br.ufu.renova.scraper.*;

import java.util.List;

/**
 * Created by yassin on 11/10/16.
 */
public class BooksPresenter implements BooksContract.Presenter {

    private BooksContract.View mView;

    private IHttpClient mHttpClient;

    public BooksPresenter(BooksContract.View booksView, IHttpClient httpClient) {
        this.mView = booksView;
        this.mHttpClient = httpClient;
    }

    @Override
    public void start() {
        mHttpClient.getBooks(new IHttpClient.GetBooksCallback() {
            @Override
            public void onComplete(List<Book> books) {
                mView.showBooksList(books);
            }

            @Override
            public void onError(Exception e) {
                Log.e(this.getClass().getName(), "", e);
            }
        });
    }

    @Override
    public void onBookClick(Book b) {
        mHttpClient.renew(b, new IHttpClient.RenewCallback() {
            @Override
            public void onComplete(Book book) {
                mHttpClient.getBooks(new IHttpClient.GetBooksCallback() {
                    @Override
                    public void onComplete(List<Book> books) {
                        mView.showBooksList(books);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(this.getClass().getName(), "", e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(this.getClass().getName(), "", e);
            }
        });
    }

    @Override
    public void onBookErrorIconClick(Book b) {
        mView.showBookErrorToast(b);
    }
}
