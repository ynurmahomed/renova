package com.renovapp.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RenovaService extends Service {
    private final IBinder mBinder = new Binder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("RenovaService", "Received start id " + startId + ": " + intent);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class Binder extends android.os.Binder {
        RenovaService getService() {
            return RenovaService.this;
        }
    }

    public void hello() {
        Log.d("RenovaService", "Hello World!");
    }
}
