package com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.Config;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().hashCode() == ("android.intent.action.BOOT_COMPLETED").hashCode()) {
            // on device boot complete, reset the alarm
            Intent alarmIntent = new Intent(context, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Config.notifTime);



            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }

            if (manager != null) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        0, pendingIntent);
            }
        }

    }
}
