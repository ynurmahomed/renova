package br.ufu.renova.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import br.ufu.renova.LoginActivity;
import br.ufu.renova.scraper.Book;
import br.ufu.renova.R;

import java.util.Arrays;

/**
 * Created by pablohenrique on 11/16/14.
 */
public class NotificationPublishReceiver extends BroadcastReceiver {

    public static final String EXTRA_BOOKS = "EXTRA_BOOKS";

    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

        Object[] tmp = (Object[]) intent.getSerializableExtra(EXTRA_BOOKS);
        Book[] books = Arrays.copyOf(tmp, tmp.length, Book[].class);

        wl.acquire();
        this.callNotification(context, books);
        wl.release();

    }

    private void callNotification(Context context, Book[] books){
        int notificationId = 1;
        int numBooks = books.length;

        Intent login = new Intent(context, LoginActivity.class);
        PendingIntent loginPendingIntent = PendingIntent.getActivity(context, 0, login, 0);

        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(soundURI)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(context.getResources().getQuantityString(R.plurals.notification_title, numBooks, numBooks))
                        .setContentText("Toque para renovar.")
                        .setContentIntent(loginPendingIntent)
                        .setAutoCancel(true);

        for (int i=0; i < books.length && i < 5; i++) {
            inboxStyle.addLine(books[i].getTitle());
        }

        notificationBuilder.setStyle(inboxStyle);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
