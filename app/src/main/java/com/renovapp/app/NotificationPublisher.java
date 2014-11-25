package com.renovapp.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.renovapp.app.scraper.Book;

/**
 * Created by pablohenrique on 11/16/14.
 */
public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        for(Book b : BooksListGlobal.getInstance().getAllBooks().values()){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = intent.getParcelableExtra(b.getTitle());
            int id = intent.getIntExtra(b.getBarcode(), 0);
            notificationManager.notify(id, notification);
        }
/*
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);

        for(String s : BooksListGlobal.getInstance().getAllBooks().keySet())
            System.out.println(": " + s);

*/
    }
}
