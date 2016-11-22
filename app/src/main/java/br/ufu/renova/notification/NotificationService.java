package br.ufu.renova.notification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import br.ufu.renova.Injection;
import br.ufu.renova.R;
import br.ufu.renova.model.Book;
import br.ufu.renova.model.User;
import br.ufu.renova.scraper.IHttpClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationService extends Service {

    private IHttpClient library;

    private SharedPreferences prefs;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationService", "Received start id " + startId + ": " + intent);

        prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String login = prefs.getString(getString(R.string.preference_login), "");
        final String password = prefs.getString(getString(R.string.preference_password), "");

        if (!(login.isEmpty() || password.isEmpty())) {
            library = Injection.provideHttpClient();
            library.login(login, password, new IHttpClient.LoginCallback() {
                @Override
                public void onComplete(User user) {
                    library.getBooks(new IHttpClient.GetBooksCallback() {
                        @Override
                        public void onComplete(List<Book> books) {
                            List<Book> toExpire = new ArrayList<Book>();

                            for(Book b: books) {
                                if (shouldNotify(b)) {
                                    toExpire.add(b);
                                }
                            }

                            if (!toExpire.isEmpty()) {
                                Intent i = new Intent(NotificationService.this, NotificationPublishReceiver.class);
                                i.putExtra(NotificationPublishReceiver.EXTRA_BOOKS, toExpire.toArray());
                                sendBroadcast(i);
                            }
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


        return Service.START_REDELIVER_INTENT;
    }

    private boolean shouldNotify(Book b) {
        int defaultValue = getResources().getInteger(R.integer.preference_notifications_default);
        int defaultDays = prefs.getInt(getString(R.string.preference_notifications), defaultValue);

        Calendar today = Calendar.getInstance();

        long diff = b.getExpiration().getTime() - today.getTimeInMillis();
        long days =  diff / (24 * 60 * 60 * 1000);

        return days <= defaultDays;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("No binding allowed");
    }
}
