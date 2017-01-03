package br.ufu.renova.scraper;

import android.os.Handler;
import br.ufu.renova.model.Book;
import br.ufu.renova.model.User;

import java.io.IOException;
import java.util.List;

/**
 * Created by yassin on 04/12/14.
 */
public class UFULibraryDataSource implements ILibraryDataSource {

    private static UFULibraryDataSource INSTANCE;

    private String patronhost;

    private String sessionId;

    private User user;

    public static UFULibraryDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UFULibraryDataSource();
        }
        return INSTANCE;
    }

    private UFULibraryDataSource() {
    }

    @Override
    public void login(final String username, final String password, final LoginCallback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    patronhost = Scraper.scrapePatronhost();
                    sessionId = Scraper.login(patronhost, username, password);
                    user = new User(username, password);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onComplete(user);
                        }
                    });
                } catch (ScrapeException | IOException | LoginException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void getBooks(final GetBooksCallback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Book> books = Scraper.scrapeBooks(sessionId);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onComplete(books);
                        }
                    });
                } catch (SessionExpiredException | ScrapeException | IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void renew(final Book b, final RenewCallback callback) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Scraper.renew(patronhost, sessionId, user.getUsername(), b);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onComplete(b);
                        }
                    });
                } catch (RenewException | IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();

    }
}
