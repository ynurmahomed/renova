package br.ufu.renova.books;

import android.util.Log;
import br.ufu.renova.model.Book;
import br.ufu.renova.scraper.*;

import java.io.IOException;
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
        loadBooks();
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
                        handleError(e, "Carregando livros após renovar.");
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(this.getClass().getName(), "Renovando livro.", e);
            }
        });
    }

    @Override
    public void onBookErrorIconClick(Book b) {
        mView.showBookErrorToast(b);
    }

    @Override
    public void onReloadClick() {
        loadBooks();
    }

    private void loadBooks() {
        mView.showProgress();
        mHttpClient.getBooks(new IHttpClient.GetBooksCallback() {
            @Override
            public void onComplete(List<Book> books) {
                if (books.size() == 0) {
                    mView.showEmptyView();
                } else {
                    mView.showBooksList(books);
                }
            }

            @Override
            public void onError(Exception e) {
                mView.showEmptyView();
                handleError(e, "Carregando livros.");
            }
        });
    }

    private void handleError(Exception e, String msg) {
        try {
            Log.e(this.getClass().getName(), msg, e);
            throw e;
        } catch (SessionExpiredException ex) {
            mView.showSessionExpiredToast();
        } catch (ScrapeException ex) {
            mView.showScrapeErrorToast();
        } catch (IOException ex) {
            mView.showNoConnectionToast();
        } catch (Exception e1) {
            // ignored
        }
    }
}
