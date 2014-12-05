package com.renovapp.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import com.renovapp.app.scraper.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class NotificationService extends Service {

    private HttpClient library;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationService", "Received start id " + startId + ": " + intent);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String login = prefs.getString(getString(R.string.preference_login), "");
        String password = prefs.getString(getString(R.string.preference_password), "");

        try {
            if (!(login.isEmpty() || password.isEmpty())) {
                library = new HttpClient(login, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("No binding allowed");
    }
}
