package com.renovapp.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yassin on 03/12/14.
 */
public class RenovaServiceStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, RenovaService.class);
        context.startService(service);
    }
}
