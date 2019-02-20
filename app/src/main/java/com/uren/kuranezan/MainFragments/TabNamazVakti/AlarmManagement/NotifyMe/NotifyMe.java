package com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.util.Log;

import com.uren.kuranezan.Interfaces.CompleteCallback;
import com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.RepeatingActivity;
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
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_ERTESI_GUN_BEFORE_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_ERTESI_GUN_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_GUNES;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IKINDI;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_OGLE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_YATSI;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_GET_PRAYER_TIMES_FROM_SERVER;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_READ_PRAYER_TIMES;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_ACTIONS;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_ACTIONS_COLLAPSE;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_ACTIONS_DISMISS;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_ACTIONS_TEXT;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_COLOR;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_CONTENT_TEXT;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_CUSTOM_ID;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_LARGE_ICON;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_LED_COLOR;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_SMALL_ICON;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_TIME;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.NOTIFICATION_TITLE_TEXT;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.Notification.NotificationEntry.TABLE_NAME;
import static com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.NotificationPublisher.NOTIFICATION_ID;

/**
 * Created by jbonk on 6/16/2018.
 */

public class NotifyMe {

    protected final Builder builder;
    private static String strSeparator = "__,__";

    private static PrayerTimes[] prayerTimesList;
    private static PrayerTimes currentDayPrayerTimes;
    private static PrayerTimes nextDayPrayerTimes;
    private static boolean downloadedFromServer = false;


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

    public static void setNotif(Context context, long time) {

        Log.i("setNotifStart", "ok");

        Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Notification.NotificationEntry.TABLE_NAME + " WHERE 1=1", null);

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

        if (myList.size() == 0) {
            createNotif(context, time);
        } else if (myList.size() == 1) {
            updateNotifTime(context, time, myList.get(0));
        } else {
            cancelAll(context, myList);
            createNotif(context, time);
        }

        Log.i("Notif set", "successful");
        getNotifId(context);

    }

    private static void getNotifId(Context context) {
        Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Notification.NotificationEntry.TABLE_NAME + " WHERE 1=1", null);

        List<Integer> myList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(Notification.NotificationEntry._ID));
                myList.add(id);
                Log.i("valid-notifID", String.valueOf(id));
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

    }

    private static void createNotif(Context context, long time) {

        Log.i("createNotif", "ok");

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(time);

        Intent intent = new Intent(context, RepeatingActivity.class);
        intent.putExtra("test", "I am a String");
        NotifyMe notifyMe = new NotifyMe.Builder(context)
                .title("Başlık1")
                .content("İçerik 1")
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .key("test")
                .addCloseAction(new Intent(), "Tamam", true)
                .large_icon(R.mipmap.ic_launcher_round)
                .build();

    }

    private static void updateNotifTime(Context context, long time, int notificationId) {

        Log.i("updateNotifTime", "ok");
        Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTIFICATION_TIME, time);
        db.update(TABLE_NAME, values, Notification.NotificationEntry._ID + " = " + notificationId, null);
        db.close();

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(time);

        scheduleNotification(context, String.valueOf(notificationId), now.getTimeInMillis());
    }

    private static void cancelAll(Context context, List<Integer> myList) {

        Log.i("cancelAll", "ok");

        try {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            for (int i = 0; i < myList.size(); i++) {
                int id = myList.get(i);
                db.delete(TABLE_NAME, Notification.NotificationEntry._ID + " = " + id, null);
                mNotificationManager.cancel(id);
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void isValid(Context context, int notificationId) {

        Notification.NotificationDBHelper mDbHelper = new Notification.NotificationDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + Notification.NotificationEntry._ID + " = " + notificationId, null);

        if (cursor.getCount() > 0) {
            Log.i("id bulundu", String.valueOf(notificationId));
        } else {
            Log.i("id bulunamadı", String.valueOf(notificationId));
        }
        cursor.close();
        db.close();
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
        Log.i("***scheduleNotification", "ok");
        Log.i("notifId", notificationId);
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

        public Builder addCloseAction(Intent intent, String text, boolean close) {
            return addAction(intent, text, close);
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

    public static void setNextNotifTime(Context context) {

        Config config = new Config();
        config.load(context);

        String x = Config.countyCode;
        Log.i("countyCode", x);

        if (!Config.countyCode.equals("")) {
            getPrayerTimesFromLocal(context);
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
            currentDayPrayerTimes = getCurrentDayPrayerTimes(context);

            if (currentDayPrayerTimes != null) {
                getNextPrayerTime(context);
            }

        }


    }

    private static PrayerTimes getCurrentDayPrayerTimes(Context context) {

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

    private static void getNextPrayerTime(Context context) {

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


            long ms;
            Calendar calendar = Calendar.getInstance();

            //Vakit onceleri
            ms = prayer1.getTime() - Config.timeBeforeGunes * 60 * 1000;
            calendar.setTimeInMillis(ms);
            Date prayer11 = calendar.getTime();

            ms = prayer2.getTime() - Config.timeBeforeOgle * 60 * 1000;
            calendar.setTimeInMillis(ms);
            Date prayer21 = calendar.getTime();

            ms = prayer3.getTime() - Config.timeBeforeIkindi * 60 * 1000;
            calendar.setTimeInMillis(ms);
            Date prayer31 = calendar.getTime();

            ms = prayer4.getTime() - Config.timeBeforeAksam * 60 * 1000;
            calendar.setTimeInMillis(ms);
            Date prayer41 = calendar.getTime();

            ms = prayer5.getTime() - Config.timeBeforeYatsi * 60 * 1000;
            calendar.setTimeInMillis(ms);
            Date prayer51 = calendar.getTime();

            ms = prayer6.getTime() - Config.timeBeforeImsak * 60 * 1000;
            calendar.setTimeInMillis(ms);
            Date prayer61 = calendar.getTime();

            Date currentDate = Calendar.getInstance().getTime();

            Log.i("imsak", currentDayPrayerTimes.getImsak());
            Log.i("gunes", currentDayPrayerTimes.getGunes());
            Log.i("ogle", currentDayPrayerTimes.getOgle());
            Log.i("ikindi", currentDayPrayerTimes.getIkindi());
            Log.i("aksam", currentDayPrayerTimes.getAksam());
            Log.i("yatsi", currentDayPrayerTimes.getYatsi());


            /* GUNES  */
            if (currentDate.compareTo(prayer1) > 0 && currentDate.compareTo(prayer21) <= 0 && Config.notifBeforeGunes) {
                target = PRAYER_TIME_BEFORE_GUNES;
                nextPrayerTimeInMillis = prayer11.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "gunes oncesi");
                return;
            }

            if (currentDate.compareTo(prayer1) > 0 && currentDate.compareTo(prayer2) <= 0 && Config.notifExactGunes) {
                target = PRAYER_TIME_GUNES; //gunes
                nextPrayerTimeInMillis = prayer2.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "gunes");
                return;
            }

            /* OGLE  */
            if (currentDate.compareTo(prayer2) > 0 && currentDate.compareTo(prayer31) <= 0 && Config.notifBeforeOgle) {
                target = PRAYER_TIME_BEFORE_OGLE;
                nextPrayerTimeInMillis = prayer31.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "öğle oncesi");
                return;
            }

            if (currentDate.compareTo(prayer2) > 0 && currentDate.compareTo(prayer3) <= 0 && Config.notifExactOgle) {
                target = PRAYER_TIME_OGLE; //gunes
                nextPrayerTimeInMillis = prayer3.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "öğle");
                return;
            }

            /* IKINDI  */
            if (currentDate.compareTo(prayer3) > 0 && currentDate.compareTo(prayer41) <= 0 && Config.notifBeforeIkindi) {
                target = PRAYER_TIME_BEFORE_IKINDI;
                nextPrayerTimeInMillis = prayer41.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "ikindi oncesi");
                return;
            }

            if (currentDate.compareTo(prayer3) > 0 && currentDate.compareTo(prayer4) <= 0 && Config.notifExactIkindi) {
                target = PRAYER_TIME_IKINDI; //gunes
                nextPrayerTimeInMillis = prayer4.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "ikindi");
                return;
            }

            /* AKSAM  */
            if (currentDate.compareTo(prayer4) > 0 && currentDate.compareTo(prayer51) <= 0 && Config.notifBeforeAksam) {
                target = PRAYER_TIME_BEFORE_AKSAM;
                nextPrayerTimeInMillis = prayer51.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "akşam oncesi");
                return;
            }


            if (currentDate.compareTo(prayer4) > 0 && currentDate.compareTo(prayer5) <= 0 && Config.notifExactAksam) {
                target = PRAYER_TIME_AKSAM; //gunes
                nextPrayerTimeInMillis = prayer5.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "akşam");
                return;
            }

            /* YATSI  */
            if (currentDate.compareTo(prayer5) > 0 && currentDate.compareTo(prayer61) <= 0 && Config.notifBeforeYatsi) {
                target = PRAYER_TIME_BEFORE_YATSI;
                nextPrayerTimeInMillis = prayer61.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "yatsı oncesi");
                return;
            }


            if (currentDate.compareTo(prayer5) > 0 && currentDate.compareTo(prayer6) <= 0 && Config.notifExactYatsi) {
                target = PRAYER_TIME_YATSI; //gunes
                nextPrayerTimeInMillis = prayer6.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "yatsı");
                return;
            }

            /* ERTESI GUN IMSAK  */
            if (currentDate.compareTo(prayer6) > 0 && (Config.notifBeforeImsak || Config.notifExactImsak)) {

                nextDayPrayerTimes = getNextDayPrayerTimes(context);
                if (nextDayPrayerTimes != null) {
                    Date prayerNextDayImsak = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getImsak());

                    ms = prayerNextDayImsak.getTime() - Config.timeBeforeImsak * 60 * 1000;
                    calendar.setTimeInMillis(ms);
                    Date prayer71 = calendar.getTime();

                    if (currentDate.compareTo(prayer71) <= 0 && Config.notifBeforeImsak) {
                        target = PRAYER_TIME_ERTESI_GUN_BEFORE_IMSAK;
                        nextPrayerTimeInMillis = prayer71.getTime();
                        setData(context, target, nextPrayerTimeInMillis);
                        Log.i("nextNotifTime", "ertesi gün imsak oncesi");
                        return;
                    }

                    if (currentDate.compareTo(prayerNextDayImsak) <= 0 && Config.notifExactImsak) {
                        target = PRAYER_TIME_ERTESI_GUN_IMSAK;
                        nextPrayerTimeInMillis = prayerNextDayImsak.getTime();
                        setData(context, target, nextPrayerTimeInMillis);
                        Log.i("nextNotifTime", "ertesi gün imsak oncesi");
                        return;
                    }
                } else {
                    if (!downloadedFromServer) {
                        getPrayerTimesFromServer(context);
                    }
                }

            }


            //Tüm bunların dışında birşey ise hedef vakit aynı gün içerisindeki imsaktır..
            if (currentDate.compareTo(prayer11) <= 0 && Config.notifBeforeImsak) {
                target = PRAYER_TIME_BEFORE_IMSAK;
                nextPrayerTimeInMillis = prayer11.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "imsak oncesi");
                return;
            }

            if (currentDate.compareTo(prayer1) <= 0 && Config.notifExactImsak) {
                target = PRAYER_TIME_IMSAK; //gunes
                nextPrayerTimeInMillis = prayer1.getTime();
                setData(context, target, nextPrayerTimeInMillis);
                Log.i("nextNotifTime", "imsak");
                return;
            }


        } catch (ParseException e) {
            Log.e("parseError", e.toString());
        }

        return;

    }

    private static void setData(Context context, int target, long nextPrayerTimeInMillis) {

        Config.notifTime = nextPrayerTimeInMillis;
        Config.targetPrayerTime = target;
        Config.updateNotif(context);
        Log.i("nextNotifTime", "set edildi");
        Log.i("notifTime", String.valueOf(Config.notifTime));
        Log.i("targetPrayerTime", String.valueOf(Config.targetPrayerTime));
        updateNotif(context);


    }


    private static void evaluateTarget(Context context) {


    }

    private static PrayerTimes getNextDayPrayerTimes(Context context) {

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

    private static void updateNotif(Context context) {

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(Config.notifTime);

        NotifyMe.setNotif(context, Config.notifTime);
        Log.i("notifTime", String.valueOf(now));

    }

}
