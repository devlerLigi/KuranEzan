package com.uren.kuranezan.MainFragments.TabNamazVakti.Notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootNotifyMe extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("neredeyiz", "BootNotifyMe");
        Log.i("COMING_FROM", "BootNotify");
        NotifyMe.setNotifications(context);
    }
}
