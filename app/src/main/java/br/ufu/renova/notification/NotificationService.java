package br.ufu.renova.notification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import br.ufu.renova.scraper.Book;
import br.ufu.renova.scraper.HttpClient;
import br.ufu.renova.scraper.LoginException;
import br.ufu.renova.R;

import java.io.IOException;
import java.util.Calendar;

public class NotificationService extends Service {

    private HttpClient library;
    private SharedPreferences prefs;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationService", "Received start id " + startId + ": " + intent);

        prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String login = prefs.getString(getString(R.string.preference_login), "");
        final String password = prefs.getString(getString(R.string.preference_password), "");

        if (!(login.isEmpty() || password.isEmpty())) {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        library = new HttpClient(login, password);

                        for(Book b: library.getBooks()) {
                            if (shouldNotify(b)) {
                                Intent i = new Intent(NotificationService.this, NotificationPublishReceiver.class);
                                i.putExtra(NotificationPublishReceiver.EXTRA_BOOK, b);
                                sendBroadcast(i);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (LoginException e) {
                        e.printStackTrace();
                    }
                }

            }).start();
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
