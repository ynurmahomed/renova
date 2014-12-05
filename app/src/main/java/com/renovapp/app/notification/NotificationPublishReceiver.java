package com.renovapp.app.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.renovapp.app.LoginActivity;
import com.renovapp.app.R;
import com.renovapp.app.scraper.Book;

/**
 * Created by pablohenrique on 11/16/14.
 */
public class NotificationPublishReceiver extends BroadcastReceiver {

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
        int notificationId = b.getNotificationId();

        Intent login = new Intent(context, LoginActivity.class);
        PendingIntent loginPendingIntent = PendingIntent.getActivity(context, 0, login, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Renove seu livro")
                        .setContentText(b.getTitle())
                        .setContentIntent(loginPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
