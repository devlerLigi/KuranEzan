package com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootNotifyMe extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("neredeyiz", "BootNotifyMe");
        NotifyMe.setNextNotifTime(context);
    }
}
