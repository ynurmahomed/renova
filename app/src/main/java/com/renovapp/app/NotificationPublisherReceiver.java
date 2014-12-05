package com.renovapp.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;
import com.renovapp.app.scraper.Book;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by pablohenrique on 11/16/14.
 */
public class NotificationPublisherReceiver extends BroadcastReceiver {

    public static final String EXTRA_BOOK = "EXTRA_BOOK";

    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

        Book b = (Book) intent.getSerializableExtra(EXTRA_BOOK);

        wl.acquire();
        this.callNotification(context, b);
        wl.release();

    }

    private void callNotification(Context context, Book b){
        int notificationId = Integer.parseInt(b.getBarcode());

        PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class), 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Renove seu livro")
                        .setContentText(b.getTitle())
                        .setContentIntent(viewPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
