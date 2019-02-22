package com.uren.kuranezan.MainFragments.TabNamazVakti.Notify;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.util.Log;

import com.uren.kuranezan.Interfaces.CompleteCallback;
import com.uren.kuranezan.MainActivity;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.PrayerTimesList;
import com.uren.kuranezan.Utils.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_AKSAM;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_BEFORE_AKSAM;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_BEFORE_GUNES;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_BEFORE_IKINDI;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_BEFORE_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_BEFORE_OGLE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_BEFORE_YATSI;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_GUNES;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IKINDI;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_OGLE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_YATSI;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_GET_PRAYER_TIMES_FROM_SERVER;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_READ_PRAYER_TIMES;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeAksam;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeBeforeGunes;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeBeforeIkindi;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeBeforeImsak;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeBeforeOgle;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeBeforeYatsi;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeBeforeAksam;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeGunes;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeIkindi;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeImsak;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeOgle;
import static com.uren.kuranezan.Constants.StringConstants.key_prayerTimeYatsi;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_ACTIONS;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_ACTIONS_COLLAPSE;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_ACTIONS_DISMISS;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_ACTIONS_TEXT;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_COLOR;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_CONTENT_TEXT;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_CUSTOM_ID;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_LARGE_ICON;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_LED_COLOR;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_SMALL_ICON;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_TIME;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.NOTIFICATION_TITLE_TEXT;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.Notification.NotificationEntry.TABLE_NAME;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.NotificationPublisher.NOTIFICATION_ID;

/**
 * Created by jbonk on 6/16/2018.
 */

public class NotifyMe {

    protected final Builder builder;
    private static String strSeparator = "__,__";
    private static String content = "";

    private static PrayerTimes[] prayerTimesList;
    private static PrayerTimes currentDayPrayerTimes;
    private static PrayerTimes nextDayPrayerTimes;
    private static boolean downloadedFromServer = false;

    //Current date prayer times notif
    private static Date currentDateImsak, currentDateGunes, currentDateOgle, currentDateIkindi, currentDateAksam, currentDateYatsi;
    private static Date currentDateBeforeImsak, currentDateBeforeGunes, currentDateBeforeOgle, currentDateBeforeIkindi, currentDateBeforeAksam, currentDateBeforeYatsi;

    //Next date prayer times notif
    private static Date nextDateImsak, nextDateGunes, nextDateOgle, nextDateIkindi, nextDateAksam, nextDateYatsi;
    private static Date nextDateBeforeImsak, nextDateBeforeGunes, nextDateBeforeOgle, nextDateBeforeIkindi, nextDateBeforeAksam, nextDateBeforeYatsi;


    protected NotifyMe(Builder builder) {
        this.builder = builder;
        Calendar cal = Calendar.getInstance();
        cal.setTime(builder.time);
        cal.add(Calendar.MILLISECOND, builder.delay);
        Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(builder.context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (builder.title == null)
            builder.title("");
        if (builder.content == null)
            builder.content("");
        if (builder.key == null)
            builder.key = "";
        values.put(NOTIFICATION_TITLE_TEXT, String.valueOf(builder.title));
        values.put(NOTIFICATION_CONTENT_TEXT, String.valueOf(builder.content));
        values.put(NOTIFICATION_TIME, cal.getTimeInMillis());
        values.put(NOTIFICATION_ACTIONS, convertArrayToString(builder.actions));
        values.put(NOTIFICATION_ACTIONS_TEXT, convertArrayToString(builder.actions_text));
        values.put(NOTIFICATION_ACTIONS_DISMISS, convertArrayToString(builder.actions_dismiss));
        values.put(NOTIFICATION_ACTIONS_COLLAPSE, convertArrayToString(builder.actions_collapse));
        values.put(NOTIFICATION_CUSTOM_ID, String.valueOf(builder.key));
        values.put(NOTIFICATION_LED_COLOR, String.valueOf(builder.led_color));
        values.put(NOTIFICATION_COLOR, builder.color);
        values.put(NOTIFICATION_SMALL_ICON, builder.small_icon);
        values.put(NOTIFICATION_LARGE_ICON, builder.large_icon);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        scheduleNotification(builder.context, String.valueOf(id), cal.getTimeInMillis());
    }


    public static String convertArrayToString(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            // Do not append comma at the end of last element
            if (i < array.length - 1) {
                str = str + strSeparator;
            }
        }
        return str;
    }

    public static String[] convertStringToArray(String str) {
        String[] arr = str.split(strSeparator);
        return arr;
    }

    public static void cancel(Context context, int notificationId) {
        try {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            db.delete(TABLE_NAME, Notification.NotificationEntry._ID + " = " + notificationId, null);
            db.close();
            mNotificationManager.cancel(notificationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancel(Context context, String key) {
        try {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE custom_id = ? LIMIT 1", new String[]{key});
            cursor.moveToFirst();
            int notificationId = cursor.getInt(cursor.getColumnIndex(Notification.NotificationEntry._ID));
            db.delete(TABLE_NAME, Notification.NotificationEntry._ID + " = " + notificationId, null);
            db.close();
            cursor.close();
            mNotificationManager.cancel(notificationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init(Context context) {
        Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE 1=1", null);
        while (cursor.moveToNext()) {
            Long time = cursor.getLong(cursor.getColumnIndex(NOTIFICATION_TIME));
            int id = cursor.getInt(cursor.getColumnIndex(Notification.NotificationEntry._ID));
            scheduleNotification(context, String.valueOf(id), time);
        }
        cursor.close();
        db.close();
    }

    public final Builder getBuilder() {
        return this.builder;
    }

    private static void scheduleNotification(Context context, String notificationId, Long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(notificationId), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        Log.i("TAKIP_SCHEDULE", "OK");
        Log.i("TAKIP_NOTIF_ID", notificationId);
    }


    public static class Builder {

        protected Context context;
        protected CharSequence title, content, key;
        protected Long id;
        protected int delay = 0;
        protected Date time = new Date();
        protected String[] actions = new String[0];
        protected String[] actions_text = new String[0];
        protected String[] actions_dismiss = new String[0];
        protected String[] actions_collapse = new String[0];
        protected int color = -1;
        protected int led_color = 0;
        protected int small_icon = -1;
        protected int large_icon = -1;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder delay(int delay) {
            this.delay = delay;
            return this;
        }

        public Builder small_icon(int small_icon) {
            this.small_icon = small_icon;
            return this;
        }

        public Builder large_icon(int large_icon) {
            this.large_icon = large_icon;
            return this;
        }

        public Builder led_color(int red, int green, int blue, int alpha) {
            int color = (alpha & 0xff) << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
            this.led_color = color;
            return this;
        }


        public Builder color(int red, int green, int blue, int alpha) {
            int color = (alpha & 0xff) << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
            this.color = color;
            return this;
        }


        public Builder addAction(Intent intent, String text) {
            return addAction(intent, text, true, true);
        }

        public Builder addAction(Intent intent, String text, boolean dismiss) {
            return addAction(intent, text, dismiss, true);
        }

        public Builder addAction(Intent intent, String text, boolean dismiss, boolean collapse) {
            String[] temp = new String[actions.length + 1];
            for (int i = 0; i < this.actions.length; i++) {
                temp[i] = this.actions[i];
            }
            String[] temp_collapse = new String[actions_collapse.length + 1];
            for (int i = 0; i < this.actions_collapse.length; i++) {
                temp_collapse[i] = this.actions_collapse[i];
            }
            String[] temp_text = new String[actions_text.length + 1];
            for (int i = 0; i < this.actions_text.length; i++) {
                temp_text[i] = this.actions_text[i];
            }
            String[] temp_dismiss = new String[actions_dismiss.length + 1];
            for (int i = 0; i < this.actions_dismiss.length; i++) {
                temp_dismiss[i] = this.actions_dismiss[i];
            }
            temp_dismiss[actions_dismiss.length] = String.valueOf(dismiss);
            temp_collapse[actions_collapse.length] = String.valueOf(collapse);
            temp_text[actions_text.length] = text;
            temp[actions.length] = intent.toUri(0);
            this.actions_text = temp_text;
            this.actions = temp;
            this.actions_dismiss = temp_dismiss;
            this.actions_collapse = temp_collapse;
            return this;
        }

        public Builder time(Date time) {
            this.time = time;
            return this;
        }

        public Builder time(Calendar time) {
            this.time = time.getTime();
            return this;
        }

        public Builder title(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder title(@StringRes int title) {
            title(this.context.getText(title));
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }


        public Builder key(CharSequence key) {
            this.key = key;
            return this;
        }

        public Builder key(@StringRes int key) {
            key(this.context.getText(key));
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder content(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder content(@StringRes int content) {
            title(this.context.getText(content));
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        @UiThread
        public NotifyMe build() {
            return new NotifyMe(this);
        }

    }

    public static void setNotifications(Context context) {

        Config config = new Config();
        config.load(context);

        if (Config.notifEnabled &&
                (Config.notifBeforeImsak || Config.notifExactImsak || Config.notifBeforeGunes || Config.notifExactGunes ||
                        Config.notifBeforeOgle || Config.notifExactOgle || Config.notifBeforeIkindi || Config.notifExactIkindi ||
                        Config.notifBeforeAksam || Config.notifExactAksam || Config.notifBeforeYatsi || Config.notifExactYatsi)) {
            Log.i("notifSituation", "open");
            if (!Config.countyCode.equals("")) {
                getPrayerTimesFromLocal(context);
            }

        } else {
            Log.i("log", "1");
            deleteAll(context);
            Log.i("notifSituation", "closed");
        }

        //printActiveNotifications(context);

    }

    private static void printActiveNotifications(Context context) {

        Log.i("TUM_NOTIFLER", "printAll");
        try {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE 1=1", null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(Notification.NotificationEntry._ID));
                String key = cursor.getString(cursor.getColumnIndex(Notification.NotificationEntry.NOTIFICATION_CUSTOM_ID));
                String time = cursor.getString(cursor.getColumnIndex(Notification.NotificationEntry.NOTIFICATION_TIME));

                Log.i("id: " + id, "key: " + key + " time: " + time);
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void getPrayerTimesFromLocal(final Context context) {
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

    private static void getPrayerTimesFromServer(final Context context) {

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

    private static void startOperation(Context context) {

        if (prayerTimesList != null) {

            currentDayPrayerTimes = setCurrentDatePrayeTimes(context);
            nextDayPrayerTimes = setNextDatePrayerTimes(context);

            if (currentDayPrayerTimes != null && nextDayPrayerTimes != null) {
                convertCurrentDatePrayerTimesToDate(context);
                convertNextDatePrayerTimesToDate(context);

                adjustNotificationTimes(context);

                printActiveNotifications(context);
            }

        }

    }

    private static PrayerTimes setCurrentDatePrayeTimes(Context context) {

        Date current = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String formattedDate = df.format(current);

        for (int i = 0; i < prayerTimesList.length; i++) {
            if (prayerTimesList[i].getMiladiTarihKisa().equals(formattedDate)) {
                return prayerTimesList[i];
            }
        }

        return null;

    }

    private static PrayerTimes setNextDatePrayerTimes(Context context) {

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

    private static void convertCurrentDatePrayerTimesToDate(Context context) {

        String pattern = "dd.MM.yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            currentDateImsak = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getImsak());
            currentDateGunes = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getGunes());
            currentDateOgle = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getOgle());
            currentDateIkindi = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getIkindi());
            currentDateAksam = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getAksam());
            currentDateYatsi = dateFormat.parse(currentDayPrayerTimes.getMiladiTarihKisa() + " " + currentDayPrayerTimes.getYatsi());

            long miliseconds;
            Calendar calendar = Calendar.getInstance();

            //Vakit onceleri
            miliseconds = currentDateImsak.getTime() - Config.timeBeforeImsak * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            currentDateBeforeImsak = calendar.getTime();


            miliseconds = currentDateGunes.getTime() - Config.timeBeforeGunes * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            currentDateBeforeGunes = calendar.getTime();

            miliseconds = currentDateOgle.getTime() - Config.timeBeforeOgle * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            currentDateBeforeOgle = calendar.getTime();

            miliseconds = currentDateIkindi.getTime() - Config.timeBeforeIkindi * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            currentDateBeforeOgle = calendar.getTime();

            miliseconds = currentDateAksam.getTime() - Config.timeBeforeAksam * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            currentDateBeforeAksam = calendar.getTime();

            miliseconds = currentDateYatsi.getTime() - Config.timeBeforeYatsi * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            currentDateBeforeYatsi = calendar.getTime();

        } catch (ParseException e) {
            Log.e("parseError", e.toString());
        }

    }

    private static void convertNextDatePrayerTimesToDate(Context context) {
        String pattern = "dd.MM.yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            nextDateImsak = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getImsak());
            nextDateGunes = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getGunes());
            nextDateOgle = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getOgle());
            nextDateIkindi = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getIkindi());
            nextDateAksam = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getAksam());
            nextDateYatsi = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getYatsi());

            long miliseconds;
            Calendar calendar = Calendar.getInstance();

            //Vakit onceleri
            miliseconds = nextDateImsak.getTime() - Config.timeBeforeImsak * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            nextDateBeforeImsak = calendar.getTime();


            miliseconds = nextDateGunes.getTime() - Config.timeBeforeGunes * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            nextDateBeforeGunes = calendar.getTime();

            miliseconds = nextDateOgle.getTime() - Config.timeBeforeOgle * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            nextDateBeforeOgle = calendar.getTime();

            miliseconds = nextDateIkindi.getTime() - Config.timeBeforeIkindi * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            nextDateBeforeOgle = calendar.getTime();

            miliseconds = nextDateAksam.getTime() - Config.timeBeforeAksam * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            nextDateBeforeAksam = calendar.getTime();

            miliseconds = nextDateYatsi.getTime() - Config.timeBeforeYatsi * 60 * 1000;
            calendar.setTimeInMillis(miliseconds);
            nextDateBeforeYatsi = calendar.getTime();

        } catch (ParseException e) {
            Log.e("parseError", e.toString());
        }

    }

    private static void adjustNotificationTimes(Context context) {

        int target = 0;
        long nextNotifTimeInMillis = 0;
        Date currentDate = Calendar.getInstance().getTime();


        //imsak öncesi
        if (Config.notifBeforeImsak) {
            if (currentDate.compareTo(currentDateBeforeImsak) <= 0) {
                nextNotifTimeInMillis = currentDateBeforeImsak.getTime();
            } else {
                nextNotifTimeInMillis = nextDateBeforeImsak.getTime();
            }
            target = PRAYER_TIME_BEFORE_IMSAK;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //imsak zamanı
        if (Config.notifExactImsak) {
            if (currentDate.compareTo(currentDateImsak) <= 0) {
                nextNotifTimeInMillis = currentDateImsak.getTime();
            } else {
                nextNotifTimeInMillis = nextDateImsak.getTime();
            }
            target = PRAYER_TIME_IMSAK;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //güneş öncesi
        if (Config.notifBeforeGunes) {
            if (currentDate.compareTo(currentDateBeforeGunes) <= 0) {
                nextNotifTimeInMillis = currentDateBeforeGunes.getTime();
            } else {
                nextNotifTimeInMillis = nextDateBeforeGunes.getTime();
            }
            target = PRAYER_TIME_BEFORE_GUNES;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //güneş zamanı
        if (Config.notifExactGunes) {
            if (currentDate.compareTo(currentDateGunes) <= 0) {
                nextNotifTimeInMillis = currentDateGunes.getTime();
            } else {
                nextNotifTimeInMillis = nextDateGunes.getTime();
            }
            target = PRAYER_TIME_GUNES;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //öğle öncesi
        if (Config.notifBeforeOgle) {
            if (currentDate.compareTo(currentDateBeforeOgle) <= 0) {
                nextNotifTimeInMillis = currentDateBeforeOgle.getTime();
            } else {
                nextNotifTimeInMillis = nextDateBeforeOgle.getTime();
            }
            target = PRAYER_TIME_BEFORE_OGLE;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //öğle zamanı
        if (Config.notifExactOgle) {
            if (currentDate.compareTo(currentDateOgle) <= 0) {
                nextNotifTimeInMillis = currentDateOgle.getTime();
            } else {
                nextNotifTimeInMillis = nextDateOgle.getTime();
            }
            target = PRAYER_TIME_OGLE;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //ikindi öncesi
        if (Config.notifBeforeIkindi) {
            if (currentDate.compareTo(currentDateBeforeIkindi) <= 0) {
                nextNotifTimeInMillis = currentDateBeforeIkindi.getTime();
            } else {
                nextNotifTimeInMillis = nextDateBeforeIkindi.getTime();
            }
            target = PRAYER_TIME_BEFORE_IKINDI;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //ikindi zamanı
        if (Config.notifExactIkindi) {
            if (currentDate.compareTo(currentDateIkindi) <= 0) {
                nextNotifTimeInMillis = currentDateIkindi.getTime();
            } else {
                nextNotifTimeInMillis = nextDateIkindi.getTime();
            }
            target = PRAYER_TIME_IKINDI;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //akşam öncesi
        if (Config.notifBeforeAksam) {
            if (currentDate.compareTo(currentDateBeforeAksam) <= 0) {
                nextNotifTimeInMillis = currentDateBeforeAksam.getTime();
            } else {
                nextNotifTimeInMillis = nextDateBeforeAksam.getTime();
            }
            target = PRAYER_TIME_BEFORE_AKSAM;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //akşam zamanı
        if (Config.notifExactAksam) {
            if (currentDate.compareTo(currentDateAksam) <= 0) {
                nextNotifTimeInMillis = currentDateAksam.getTime();
            } else {
                nextNotifTimeInMillis = nextDateAksam.getTime();
            }
            target = PRAYER_TIME_AKSAM;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //yatsı öncesi
        if (Config.notifBeforeYatsi) {
            if (currentDate.compareTo(currentDateBeforeYatsi) <= 0) {
                nextNotifTimeInMillis = currentDateBeforeYatsi.getTime();
            } else {
                nextNotifTimeInMillis = nextDateBeforeYatsi.getTime();
            }
            target = PRAYER_TIME_BEFORE_YATSI;
            mapNotification(context, target, nextNotifTimeInMillis);
        }

        //yatsı zamanı
        if (Config.notifExactYatsi) {
            if (currentDate.compareTo(currentDateYatsi) <= 0) {
                nextNotifTimeInMillis = currentDateYatsi.getTime();
            } else {
                nextNotifTimeInMillis = nextDateYatsi.getTime();
            }
            target = PRAYER_TIME_YATSI;
            mapNotification(context, target, nextNotifTimeInMillis);
        }


    }

    private static void mapNotification(Context context, int target, long nextNotifTimeInMillis) {

        String key = getKeyFromTarget(context, target);
        List<Integer> notifList = getNotifList(context, key);

        if (notifList.size() == 0) {
            createNotif(context, key, nextNotifTimeInMillis);
        } else if (notifList.size() == 1) {
            updateNotif(context, notifList.get(0), nextNotifTimeInMillis, key);
        } else {
            deleteAll(context, notifList);
            createNotif(context, key, nextNotifTimeInMillis);
        }

    }

    private static List<Integer> getNotifList(Context context, String key) {

        try {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE custom_id = ? ", new String[]{key});

            List<Integer> myList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(Notification.NotificationEntry._ID));
                    myList.add(id);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.close();

            return myList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getKeyFromTarget(Context context, int target) {

        if (target == PRAYER_TIME_IMSAK) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[0] + " " + resources.getString(R.string.time);
            return key_prayerTimeImsak;
        } else if (target == PRAYER_TIME_GUNES) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[1] + " " + resources.getString(R.string.time);
            return key_prayerTimeGunes;
        } else if (target == PRAYER_TIME_OGLE) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[2] + " " + resources.getString(R.string.time);
            return key_prayerTimeOgle;
        } else if (target == PRAYER_TIME_IKINDI) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[3] + " " + resources.getString(R.string.time);
            return key_prayerTimeIkindi;
        } else if (target == PRAYER_TIME_AKSAM) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[4] + " " + resources.getString(R.string.time);
            return key_prayerTimeAksam;
        } else if (target == PRAYER_TIME_YATSI) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[5] + " " + resources.getString(R.string.time);
            return key_prayerTimeYatsi;
        } else if (target == PRAYER_TIME_BEFORE_IMSAK) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[0] + " " + resources.getString(R.string.time)
                    + " - " + Config.timeBeforeImsak + " " + resources.getString(R.string.minutesRemain);
            return key_prayerTimeBeforeImsak;
        } else if (target == PRAYER_TIME_BEFORE_GUNES) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[1] + " " + resources.getString(R.string.time)
                    + " - " + Config.timeBeforeGunes + " " + resources.getString(R.string.minutesRemain);
            return key_prayerTimeBeforeGunes;
        } else if (target == PRAYER_TIME_BEFORE_OGLE) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[2] + " " + resources.getString(R.string.time)
                    + " - " + Config.timeBeforeOgle + " " + resources.getString(R.string.minutesRemain);
            return key_prayerTimeBeforeOgle;
        } else if (target == PRAYER_TIME_BEFORE_IKINDI) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[3] + " " + resources.getString(R.string.time)
                    + " - " + Config.timeBeforeIkindi + " " + resources.getString(R.string.minutesRemain);
            return key_prayerTimeBeforeIkindi;
        } else if (target == PRAYER_TIME_BEFORE_AKSAM) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[4] + " " + resources.getString(R.string.time)
                    + " - " + Config.timeBeforeAksam + " " + resources.getString(R.string.minutesRemain);
            return key_prayerTimeBeforeAksam;
        } else if (target == PRAYER_TIME_BEFORE_YATSI) {
            Resources resources = context.getResources();
            content = resources.getStringArray(R.array.prayerTimes)[5] + " " + resources.getString(R.string.time)
                    + " - " + Config.timeBeforeYatsi + " " + resources.getString(R.string.minutesRemain);
            return key_prayerTimeBeforeYatsi;
        } else {
            return "undefined";
        }

    }

    /**********************************************************/
    private static void createNotif(Context context, String key, long nextNotifTimeInMillis) {

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(nextNotifTimeInMillis);
        String title = context.getResources().getString(R.string.remainder);
        String actionDismiss = context.getResources().getString(R.string.dismiss);
        String actionDisplay = context.getResources().getString(R.string.display);

        Log.i("TAKIP_OPERATION", "CREATE_NOTIF");
        Log.i("TAKIP_KEY", key);
        Log.i("TAKIP_NOTIFTARIHI", now.toString());

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("Extra", "Ezan Kuran");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NotifyMe notifyMe = new Builder(context)
                .title(title)
                .content(content)
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .key(key)
                .addAction(new Intent(), actionDismiss, true, false)
                .addAction(intent, actionDisplay)
                .large_icon(R.drawable.app_icon)
                .small_icon(R.drawable.app_icon)
                .build();

    }


    private static void updateNotif(Context context, Integer notificationId, long nextNotifTimeInMillis, String key) {

        Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_TIME, nextNotifTimeInMillis);
        values.put(NOTIFICATION_CONTENT_TEXT, content);
        db.update(TABLE_NAME, values, Notification.NotificationEntry._ID + " = " + notificationId, null);
        db.close();

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(nextNotifTimeInMillis);

        Log.i("TAKIP_OPERATION", "UPDATE_NOTIF");
        Log.i("TAKIP_KEY", key);
        Log.i("TAKIP_NOTIFTARIHI", now.toString());

        scheduleNotification(context, String.valueOf(notificationId), now.getTimeInMillis());
    }


    private static void deleteAll(Context context, List<Integer> notifList) {

        try {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            for (int i = 0; i < notifList.size(); i++) {
                int id = notifList.get(i);
                db.delete(TABLE_NAME, Notification.NotificationEntry._ID + " = " + id, null);
                mNotificationManager.cancel(id);
            }

            db.close();
            Log.i("deleteAll", "ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteAll(Context context) {

        try {

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE 1=1", null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(Notification.NotificationEntry._ID));
                db.delete(TABLE_NAME, Notification.NotificationEntry._ID + " = " + id, null);
                mNotificationManager.cancel(id);
            }

            cursor.close();
            db.close();
            Log.i("deleteAll", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("deleteAll-Error", e.toString());
        }
    }

    /**********************************************************/

    public static int getSound(Context context, String key) {
        Config config = new Config();
        config.load(context);

        int soundId;

        if (key.equals(key_prayerTimeBeforeImsak)) {
            soundId = Config.melodyBeforeImsak;
        } else if (key.equals(key_prayerTimeImsak)) {
            soundId = Config.melodyImsak;
        } else if (key.equals(key_prayerTimeBeforeGunes)) {
            soundId = Config.melodyBeforeGunes;
        } else if (key.equals(key_prayerTimeGunes)) {
            soundId = Config.melodyGunes;
        } else if (key.equals(key_prayerTimeBeforeOgle)) {
            soundId = Config.melodyBeforeOgle;
        } else if (key.equals(key_prayerTimeOgle)) {
            soundId = Config.melodyOgle;
        } else if (key.equals(key_prayerTimeBeforeIkindi)) {
            soundId = Config.melodyBeforeIkindi;
        } else if (key.equals(key_prayerTimeIkindi)) {
            soundId = Config.melodyIkindi;
        } else if (key.equals(key_prayerTimeBeforeAksam)) {
            soundId = Config.melodyBeforeAksam;
        } else if (key.equals(key_prayerTimeAksam)) {
            soundId = Config.melodyAksam;
        } else if (key.equals(key_prayerTimeBeforeYatsi)) {
            soundId = Config.melodyBeforeYatsi;
        } else if (key.equals(key_prayerTimeYatsi)) {
            soundId = Config.melodyYatsi;
        } else {
            soundId = 0;
        }

        return soundId;
    }

    public static int getRawItem(int soundId) {

        final int[] rawID =
                {0,
                        R.raw.notif_nice_ringtone,
                        R.raw.notif_bird_chirping,
                        R.raw.notif_beatiful_islam,
                        R.raw.notif_islam,
                        R.raw.notif_al_habib_almostafa,
                        R.raw.notif_allahu_allahu,
                        R.raw.notif_ayat_al_kursi,
                        R.raw.notif_bismillah,
                        R.raw.notif_bismillah_2,
                        R.raw.notif_bismillahillezi,
                        R.raw.notif_ezan1,
                        R.raw.notif_ezan2,
                        R.raw.notif_ezan3,
                        R.raw.notif_fajar_alarm,
                        R.raw.notif_haj_labbaik,
                        R.raw.notif_99_names_of_allah_1,
                        R.raw.notif_quran
                };

        return rawID[soundId];

    }

    public static void setDummyNotif(Context context) {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 1);

        String title = context.getResources().getString(R.string.remainder);
        String actionDismiss = context.getResources().getString(R.string.dismiss);
        String actionDisplay = context.getResources().getString(R.string.display);

        Log.i("dummyNotif", "ok");

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("test", "I am a String");
        NotifyMe notifyMe = new NotifyMe.Builder(context)
                .title(title)
                .content("Dummy Notif")
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .key("dummy")

                .addAction(new Intent(), actionDismiss, true, false)
                .addAction(intent, actionDisplay)

                .large_icon(R.mipmap.app_icon_old)
                .build();

    }

}
