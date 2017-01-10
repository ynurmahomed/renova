package br.ufu.renova.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import br.ufu.renova.Injection;

import java.util.Calendar;

/**
 * Escalona o serviço de notificações para correr a cada intervalo de {@code REPEAT_TIME}.
 *
 * @author pablohenrique on 11/24/14.
 */
public class NotificationServiceScheduleReceiver extends BroadcastReceiver {

    private static final long REPEAT_TIME = Injection.provideNotificationRepeatTime();

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmService = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, NotificationServiceStartReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 30);

        // doesnt trigger right away!
        alarmService.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pending);
    }
}