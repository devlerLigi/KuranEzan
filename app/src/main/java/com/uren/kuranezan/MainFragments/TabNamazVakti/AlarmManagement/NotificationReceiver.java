package com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.uren.kuranezan.Interfaces.CompleteCallback;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.PrayerTimesList;
import com.uren.kuranezan.Utils.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_AKSAM;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_ERTESI_GUN_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_GUNES;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IKINDI;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_OGLE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_YATSI;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_GET_PRAYER_TIMES_FROM_SERVER;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_READ_PRAYER_TIMES;

public class NotificationReceiver extends BroadcastReceiver {

    private PrayerTimes[] prayerTimesList;
    private PrayerTimes currentDayPrayerTimes;
    private PrayerTimes nextDayPrayerTimes;
    private boolean downloadedFromServer = false;

    @Override
    public void onReceive(Context context, Intent ıntent) {



        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, RepeatingActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingI = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Daily Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Daily Notification");
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder b = new NotificationCompat.Builder(context, "default");
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_more)
                .setTicker("{Time to watch some cool stuff!}")
                .setContentTitle("My Cool App")
                .setContentText("Time to watch some cool stuff!")
                .setContentInfo("INFO")
                .setContentIntent(pendingI);

        if (nm != null) {

            nm.notify(1, b.build());


            Config config = new Config();
            config.load(context);
            setNextNotifTime(context);
            setNotif(context);



        }

    }

    private void setNotif(Context context) {
        Boolean dailyNotify = true;
        PackageManager pm = context.getPackageManager();
        ComponentName receiver = new ComponentName(context, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

// if user enabled daily notifications
        if (dailyNotify) {
            //region Enable Daily Notifications
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Config.notifTime);
/*
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) +1);
            calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
*/
            // if notification time is before selected time, send notification the next day
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1);
            }
            if (manager != null) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        0, pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
            //To enable Boot Receiver class

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

            //endregion
        } else { //Disable Daily Notifications
            if (PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT) != null && manager != null) {
                manager.cancel(pendingIntent);
                //Toast.makeText(this,"Notifications were disabled",Toast.LENGTH_SHORT).show();
            }

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);

        }
    }

    private void setNextNotifTime(Context context) {
        if (!Config.countyCode.equals("")) {
            getPrayerTimesFromLocal(context);
        }
    }

    private void getPrayerTimesFromLocal(final Context context) {
        PrayerTimesList.getInstance().getAsyncPrayerTimes(context, new CompleteCallback<PrayerTimes[]>() {
            @Override
            public void onComplete(PrayerTimes[] p) {

                if (p != null) {
                    prayerTimesList = p;
                    Log.i("prayerTime ", "lokalden alindi");
                    startOperation(context);
                } else {
                    Log.i("prayerTime ", "lokalden alinamadi");
                    getPrayerTimesFromServer(context);
                }

            }

            @Override
            public void onFailed(Exception e) {
                Log.e("error", e.toString());
                getPrayerTimesFromServer(context);
            }
        }, REQUEST_TYPE_READ_PRAYER_TIMES, Config.countyCode);

    }

    private void getPrayerTimesFromServer(final Context context) {

        PrayerTimesList.getInstance().getAsyncPrayerTimes(context, new CompleteCallback<PrayerTimes[]>() {
            @Override
            public void onComplete(PrayerTimes[] p) {
                if (p != null) {
                    Log.i("prayerTime ", "serverdan alindi");
                    downloadedFromServer = true;
                    prayerTimesList = p;
                    startOperation(context);
                } else {
                    Log.i("prayerTime ", "serverdan alinamadi");
                }
            }

            @Override
            public void onFailed(Exception e) {
            }
        }, REQUEST_TYPE_GET_PRAYER_TIMES_FROM_SERVER, Config.countyCode);

    }

    private void startOperation(Context context) {

        if (prayerTimesList != null) {
            currentDayPrayerTimes = getCurrentDayPrayerTimes(context);

            if (currentDayPrayerTimes != null) {
                getNextPrayerTime(context);
            }

        }


    }


    private PrayerTimes getCurrentDayPrayerTimes(Context context) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        for (int i = 0; i < prayerTimesList.length; i++) {
            if (prayerTimesList[i].getMiladiTarihKisa().equals(formattedDate)) {

                prayerTimesList[i].setImsak("06:31");
                prayerTimesList[i].setGunes("06:32");
                prayerTimesList[i].setOgle("06:33");
                prayerTimesList[i].setIkindi("06:34");
                prayerTimesList[i].setAksam("06:35");
                prayerTimesList[i].setYatsi("06:36");


                return prayerTimesList[i];
            }
        }

        return null;
    }

    private int getNextPrayerTime(Context context) {

        int target = 0;
        long nextPrayerTimeInMillis = 0;

        String pattern = "dd.MM.yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            //Date one = dateFormat.parse(currentTime);
            Date prayer1 = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getImsak());
            Date prayer2 = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getGunes());
            Date prayer3 = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getOgle());
            Date prayer4 = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getIkindi());
            Date prayer5 = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getAksam());
            Date prayer6 = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getYatsi());


            Date currentDate = Calendar.getInstance().getTime();

            Log.i("imsak", currentDayPrayerTimes.getImsak());
            Log.i("gunes", currentDayPrayerTimes.getGunes());
            Log.i("ogle", currentDayPrayerTimes.getOgle());
            Log.i("ikindi", currentDayPrayerTimes.getIkindi());
            Log.i("aksam", currentDayPrayerTimes.getAksam());
            Log.i("yatsi", currentDayPrayerTimes.getYatsi());

            if (currentDate.compareTo(prayer1) > 0 && currentDate.compareTo(prayer2) <= 0) {
                target = PRAYER_TIME_GUNES; //gunes
                nextPrayerTimeInMillis = prayer2.getTime();
                Log.i("nextNotifTime", "gunes");
            } else if (currentDate.compareTo(prayer2) > 0 && currentDate.compareTo(prayer3) <= 0) {
                target = PRAYER_TIME_OGLE; // ogle

                nextPrayerTimeInMillis = prayer3.getTime();
                Log.i("nextNotifTime", "öğle");
            } else if (currentDate.compareTo(prayer3) > 0 && currentDate.compareTo(prayer4) <= 0) {
                target = PRAYER_TIME_IKINDI; //ikindi

                nextPrayerTimeInMillis = prayer4.getTime();
                Log.i("nextNotifTime", "ikindi");
            } else if (currentDate.compareTo(prayer4) > 0 && currentDate.compareTo(prayer5) <= 0) {
                target = PRAYER_TIME_AKSAM; //aksam
                nextPrayerTimeInMillis = prayer5.getTime();
                Log.i("nextNotifTime", "akşam");
            } else if (currentDate.compareTo(prayer5) > 0 && currentDate.compareTo(prayer6) <= 0) {
                target = PRAYER_TIME_YATSI; //yatsi

                nextPrayerTimeInMillis = prayer6.getTime();
                Log.i("nextNotifTime", "yatsı");
            } else if (currentDate.compareTo(prayer6) > 0) {
                target = PRAYER_TIME_ERTESI_GUN_IMSAK; // ertesi gun imsak

               nextDayPrayerTimes = getNextDayPrayerTimes(context);

                if (nextDayPrayerTimes != null) {
                    Date prayerNextDayImsak = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getImsak());

                    nextPrayerTimeInMillis = prayerNextDayImsak.getTime();
                    Log.i("nextNotifTime", "ertesi gün imsak");
                } else {
                    if (!downloadedFromServer) {
                        getPrayerTimesFromServer(context);
                    }
                }

            } else {
                target = PRAYER_TIME_IMSAK; //imsak

                nextPrayerTimeInMillis = prayer1.getTime();
            }

            Config.notifTime = nextPrayerTimeInMillis;
            Config.updateNotif(context);
            Log.i("nextNotifTime", "set edildi");
            Log.i("notifTime", String.valueOf(Config.notifTime));

        } catch (ParseException e) {
            Log.e("parseError", e.toString());
        }

        return target;

    }

    private PrayerTimes getNextDayPrayerTimes(Context context) {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDate = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String formattedDate = df.format(nextDate);

        for (int i = 0; i < prayerTimesList.length; i++) {
            if (prayerTimesList[i].getMiladiTarihKisa().equals(formattedDate)) {
                return prayerTimesList[i];
            }
        }

        return null;

    }


}
