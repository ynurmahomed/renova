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
import br.ufu.renova.scraper.ILibraryDataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotificationService extends Service {

    private ILibraryDataSource mDataSource;

    private SharedPreferences mPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationService", "Received start id " + startId + ": " + intent);

        mPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String login = mPreferences.getString(getString(R.string.preference_login), "");
        final String password = mPreferences.getString(getString(R.string.preference_password), "");

        if (!(login.isEmpty() || password.isEmpty())) {
            mDataSource = Injection.provideDataSource();
            mDataSource.login(login, password, new ILibraryDataSource.LoginCallback() {
                @Override
                public void onComplete(User user) {
                    mDataSource.getBooks(new ILibraryDataSource.GetBooksCallback() {
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
        int defaultDays = mPreferences.getInt(getString(R.string.preference_notifications), defaultValue);

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
