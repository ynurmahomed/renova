package br.ufu.renova.books;

import android.os.AsyncTask;
import android.util.Log;
import br.ufu.renova.scraper.*;

import java.io.IOException;

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
        // TODO: Tirar da main thread.
        // Fazer o getBooks asyncrono e pegar resultado com callbacks.
        // Acabar com AsyncTasks.
        try {
            mView.showBooksList(mHttpClient.getBooks());
        } catch (IOException | SessionExpiredException |
                ScrapeException e) {
            Log.e(this.getClass().getName(), "", e);
        }
    }

    @Override
    public void onBookClick(Book b) {
        new RenewTask().execute(b);
    }

    @Override
    public void onBookErrorIconClick(Book b) {
        mView.showBookErrorToast(b);
    }

    private class RenewTask extends AsyncTask<Book, Void, Void> {

        @Override
        protected Void doInBackground(Book... params) {
            Book b = params[0];

            try {
                mHttpClient.renew(b);
            } catch (IOException | RenewException e) {
                Log.e(this.getClass().getName(), "", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {
            try {
                mView.showBooksList(mHttpClient.getBooks());
            } catch (IOException | SessionExpiredException | ScrapeException e) {
                Log.e(this.getClass().getName(), "", e);
            }
        }
    }
}
