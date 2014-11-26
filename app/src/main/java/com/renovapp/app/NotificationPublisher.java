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
public class NotificationPublisher extends BroadcastReceiver {

    //public static String NOTIFICATION_ID = "notification-id";
    //public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        this.callNotification(context);
        wl.release();
        this.setAlarm(context);
    }

    private boolean checkDate(Book b){
        //int days = b.getExpiration().compareTo(Calendar.getInstance().getTime());
        int days = Calendar.getInstance().getTime().compareTo(b.getExpiration());
        int daysDefault = BooksListGlobal.getInstance().getAlertDays();
        if(days < 0 && daysDefault >= (days * -1) || days == 0 )
            return true;
        return false;
    }

    private void callNotification(Context context){
        for(Book b : BooksListGlobal.getInstance().getBookList()) {
            if(this.checkDate(b)) {
                int notificationId = Integer.parseInt(b.getBarcode());
                //PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);
                PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class), 0);

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("Renove seu(s) livro(s)")
                                .setContentText(b.getTitle())
                                .setContentIntent(viewPendingIntent);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(notificationId, notificationBuilder.build());
            }
        }
    }

    private void setAlarm(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, NotificationPublisher.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 20);
        //cal.add(Calendar.SECOND, 30);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), cal.getTimeInMillis(), pi); // Millisec * Second * Minute
    }
}
