package com.uren.kuranezan.MainFragments.TabNamazVakti;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uren.kuranezan.Interfaces.CompleteCallback;
import com.uren.kuranezan.Interfaces.NamazVaktiCallback;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.DeviceBootReceiver;
import com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotificationReceiver;
import com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotifyMe.NotifyMe;
import com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.RepeatingActivity;
import com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses.NamazHelper;
import com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments.BaseListFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments.ImsakiyeFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments.SettingsFragment;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.PrayerTimesList;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;
import com.uren.kuranezan.Utils.Config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_ULKE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_AKSAM;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_ERTESI_GUN_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_GUNES;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IKINDI;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_OGLE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_YATSI;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_GET_PRAYER_TIMES_FROM_SERVER;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_READ_PRAYER_TIMES;

public class NamazVaktiFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    //@BindView(R.id.txtToolbarTitle)
    //TextView txtToolbarTitle;
    @BindView(R.id.imgBackground)
    ImageView imgBackground;
    @BindView(R.id.imgDarkGradient)
    ImageView imgDarkGradient;

    //View 1
    @BindView(R.id.txtRemaining)
    TextView txtRemaining;

    //View 2
    @BindView(R.id.txtCountry)
    TextView txtCountry;
    @BindView(R.id.txtCity)
    TextView txtCity;
    @BindView(R.id.txtCounty)
    TextView txtCounty;
    @BindView(R.id.txtChangeLocation)
    TextView txtChangeLocation;

    //View 3
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtImsakiye)
    TextView txtImsakiye;
    @BindView(R.id.txtImsakTime)
    TextView txtImsakTime;
    @BindView(R.id.txtGunesTime)
    TextView txtGunesTime;
    @BindView(R.id.txtOgleTime)
    TextView txtOgleTime;
    @BindView(R.id.txtIkindiTime)
    TextView txtIkindiTime;
    @BindView(R.id.txtAksamTime)
    TextView txtAksamTime;
    @BindView(R.id.txtYatsiTime)
    TextView txtYatsiTime;

    @BindView(R.id.relImsak)
    RelativeLayout relImsak;
    @BindView(R.id.relGunes)
    RelativeLayout relGunes;
    @BindView(R.id.relOgle)
    RelativeLayout relOgle;
    @BindView(R.id.relIkindi)
    RelativeLayout relIkindi;
    @BindView(R.id.relAksam)
    RelativeLayout relAksam;
    @BindView(R.id.relYatsi)
    RelativeLayout relYatsi;

    @BindView(R.id.relLocationSelected)
    RelativeLayout relLocationSelected;
    @BindView(R.id.relLocationUnselected)
    RelativeLayout relLocationUnselected;
    @BindView(R.id.btnSelectLocation)
    Button btnSelectLocation;

    @BindView(R.id.txtAlarmSettings)
    TextView txtAlarmSettings;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 60000;

    private PrayerTimes[] prayerTimes;
    private boolean downloadedFromServer = false;
    private boolean prayerTimesSet = false;
    private long targetDifference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_namaz_vakti, container, false);
            ButterKnife.bind(this, mView);

            setUI();
            init();

            if (!Config.countyCode.equals("")) {
                setLocationInfo();
                getPrayerTimesFromLocal();
            } else {
                //no county selected before
                changeUI();
            }


        }

        return mView;
    }

    private void setNotif() {
        Boolean dailyNotify = true;
        PackageManager pm = getContext().getPackageManager();
        ComponentName receiver = new ComponentName(getContext(), DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

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
            if (PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT) != null && manager != null) {
                manager.cancel(pendingIntent);
                //Toast.makeText(this,"Notifications were disabled",Toast.LENGTH_SHORT).show();
            }

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);

        }
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.VISIBLE);
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (prayerTimesSet) {

        }


    }

    private void setUI() {
/*
        Glide.with(getContext())
                .load(R.drawable.wallpaper7)
                .apply(RequestOptions.centerCropTransform())
                .into(imgBackground);

        Glide.with(getContext())
                .load(R.drawable.gradient_dark)
                .apply(RequestOptions.centerCropTransform())
                .into(imgDarkGradient);
*/

    }

    private void changeUI() {
        relLocationSelected.setVisibility(View.GONE);
        relLocationUnselected.setVisibility(View.VISIBLE);

        btnSelectLocation.setOnClickListener(this);
    }

    private void init() {
        setRefreshListener();
        txtChangeLocation.setOnClickListener(this);
        txtImsakiye.setOnClickListener(this);
        txtAlarmSettings.setOnClickListener(this);
    }

    private void setLocationInfo() {

        relLocationUnselected.setVisibility(View.GONE);
        relLocationSelected.setVisibility(View.VISIBLE);

        txtCountry.setText(Config.country);
        txtCity.setText(Config.city);
        txtCounty.setText(Config.county);
    }


    private void getPrayerTimesFromLocal() {
        PrayerTimesList.getInstance().getAsyncPrayerTimes(getActivity(), new CompleteCallback<PrayerTimes[]>() {
            @Override
            public void onComplete(PrayerTimes[] p) {

                if (p != null) {
                    Log.i("prayerTime ", "lokalden alindi");
                    prayerTimes = p;
                    setPrayerTimes();
                    updateCountDown();
                } else {
                    Log.i("prayerTime ", "lokalden alinamadi");
                    getPrayerTimesFromServer();
                }

            }

            @Override
            public void onFailed(Exception e) {
                Log.e("error", e.toString());
                getPrayerTimesFromServer();
            }
        }, REQUEST_TYPE_READ_PRAYER_TIMES, Config.countyCode);

    }

    private void getPrayerTimesFromServer() {

        PrayerTimesList.getInstance().getAsyncPrayerTimes(getActivity(), new CompleteCallback<PrayerTimes[]>() {
            @Override
            public void onComplete(PrayerTimes[] p) {
                if (p != null) {
                    prayerTimes = p;
                    downloadedFromServer = true;
                    Log.i("prayerTime ", "serverdan alindi");
                    setPrayerTimes();
                    updateCountDown();

                } else {
                    Log.i("prayerTime ", "serverdan alinamadi");
                }
            }

            @Override
            public void onFailed(Exception e) {
            }
        }, REQUEST_TYPE_GET_PRAYER_TIMES_FROM_SERVER, Config.countyCode);

    }

    /***************************************************************************/

    private void startTimer() {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateCountDown();
                Log.i("info", "Timer start again");
            }
        }.start();

        mTimerRunning = true;

    }

    private void updateCountDownText() {

        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        int minutes = (int) ((mTimeLeftInMillis / (1000 * 60)) % 60);
        int hours = (int) ((mTimeLeftInMillis / (1000 * 60 * 60)) % 24);

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        txtRemaining.setText(timeLeftFormatted);

    }

    private void setRefreshListener() {
        NamazHelper.NamazVaktiRefresh.getInstance().setNamazVaktiCallback(new NamazVaktiCallback() {
            @Override
            public void onNamazVaktiRefresh() {
                if (PrayerTimesList.getInstance().getPrayerTimes() != null) {
                    setLocationInfo();
                    setPrayerTimes();
                    updateCountDown();
                }
            }
        });
    }

    private void setPrayerTimes() {

        PrayerTimesList instance = PrayerTimesList.getInstance();

        if (instance != null && instance.getPrayerTimes() != null) {
            prayerTimes = instance.getPrayerTimes();

            PrayerTimes currentDate = getCurrentPrayerTime();
            if (currentDate != null) {
                txtDate.setText(currentDate.getMiladiTarihKisa());
                txtImsakTime.setText(currentDate.getImsak());
                txtGunesTime.setText(currentDate.getGunes());
                txtOgleTime.setText(currentDate.getOgle());
                txtIkindiTime.setText(currentDate.getIkindi());
                txtAksamTime.setText(currentDate.getAksam());
                txtYatsiTime.setText(currentDate.getYatsi());

                prayerTimesSet = true;
            } else {
                Log.i("gunKontrolu", "lokaldeki dosyada geçerli gün yok!");
                if (!downloadedFromServer) {
                    getPrayerTimesFromServer();
                }
            }

        }

    }


    private PrayerTimes getCurrentPrayerTime() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String x = Calendar.HOUR + ":" + Calendar.MINUTE;

        for (int i = 0; i < prayerTimes.length; i++) {
            if (prayerTimes[i].getMiladiTarihKisa().equals(formattedDate)) {
                return prayerTimes[i];
            }
        }

        return null;
    }

    @Override
    public void onClick(View view) {

        if (view == txtChangeLocation) {
            changeLocationClicked();
        }

        if (view == txtImsakiye) {
            ImsakiyeClicked();
        }

        if (view == btnSelectLocation) {
            changeLocationClicked();
        }

        if (view == txtAlarmSettings) {
            settingsClicked();

        }

    }

    private void changeLocationClicked() {
        String title = getString(R.string.country);
        mFragmentNavigation.pushFragment(BaseListFragment.newInstance(ITEM_TYPE_ULKE, 0, title));
    }

    private void ImsakiyeClicked() {
        if (PrayerTimesList.getInstance() != null && PrayerTimesList.getInstance().getPrayerTimes() != null) {
            String title = getString(R.string.imsakiye);
            mFragmentNavigation.pushFragment(ImsakiyeFragment.newInstance(title));
        }
    }

    private void settingsClicked() {
        mFragmentNavigation.pushFragment(SettingsFragment.newInstance());
    }


    private void updateCountDown() {

        PrayerTimes prayerTimes = getCurrentPrayerTime();

        if (prayerTimes != null) {
            int targetPrayerTime = getNextPrayerTime(prayerTimes);

            mTimeLeftInMillis = targetDifference;
            startTimer();
        }


        /*
        if(targetPrayerTime == PRAYER_TIME_IMSAK){

        }else if(targetPrayerTime == PRAYER_TIME_GUNES){

        }else if(targetPrayerTime == PRAYER_TIME_OGLE){

        }else if(targetPrayerTime == PRAYER_TIME_IKINDI){

        }else if(targetPrayerTime == PRAYER_TIME_AKSAM){

        }else if(targetPrayerTime == PRAYER_TIME_YATSI){

        }else if(targetPrayerTime == PRAYER_TIME_ERTESI_GUN_IMSAK){

        }else{
            //..
        }
        */

    }

    private int getNextPrayerTime(PrayerTimes currentPrayerTime) {

        int target = 0;
        long nextPrayerTimeInMillis = 0;

        String pattern = "dd.MM.yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            //Date one = dateFormat.parse(currentTime);
            Date prayer1 = dateFormat.parse(currentPrayerTime.getMiladiTarihKisa() + " " + currentPrayerTime.getImsak());
            Date prayer2 = dateFormat.parse(currentPrayerTime.getMiladiTarihKisa() + " " + currentPrayerTime.getGunes());
            Date prayer3 = dateFormat.parse(currentPrayerTime.getMiladiTarihKisa() + " " + currentPrayerTime.getOgle());
            Date prayer4 = dateFormat.parse(currentPrayerTime.getMiladiTarihKisa() + " " + currentPrayerTime.getIkindi());
            Date prayer5 = dateFormat.parse(currentPrayerTime.getMiladiTarihKisa() + " " + currentPrayerTime.getAksam());
            Date prayer6 = dateFormat.parse(currentPrayerTime.getMiladiTarihKisa() + " " + currentPrayerTime.getYatsi());

            clearBackgrounds();

            Date currentDate = Calendar.getInstance().getTime();

            if (currentDate.compareTo(prayer1) > 0 && currentDate.compareTo(prayer2) <= 0) {
                target = PRAYER_TIME_GUNES; //gunes
                relGunes.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer2.getTime() - currentDate.getTime();
                nextPrayerTimeInMillis = prayer2.getTime();
            } else if (currentDate.compareTo(prayer2) > 0 && currentDate.compareTo(prayer3) <= 0) {
                target = PRAYER_TIME_OGLE; // ogle
                relOgle.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer3.getTime() - currentDate.getTime();
                nextPrayerTimeInMillis = prayer3.getTime();
            } else if (currentDate.compareTo(prayer3) > 0 && currentDate.compareTo(prayer4) <= 0) {
                target = PRAYER_TIME_IKINDI; //ikindi
                relIkindi.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer4.getTime() - currentDate.getTime();
                nextPrayerTimeInMillis = prayer4.getTime();
            } else if (currentDate.compareTo(prayer4) > 0 && currentDate.compareTo(prayer5) <= 0) {
                target = PRAYER_TIME_AKSAM; //aksam
                relAksam.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer5.getTime() - currentDate.getTime();
                nextPrayerTimeInMillis = prayer5.getTime();
            } else if (currentDate.compareTo(prayer5) > 0 && currentDate.compareTo(prayer6) <= 0) {
                target = PRAYER_TIME_YATSI; //yatsi
                relYatsi.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer6.getTime() - currentDate.getTime();
                nextPrayerTimeInMillis = prayer6.getTime();
            } else if (currentDate.compareTo(prayer6) > 0) {
                target = PRAYER_TIME_ERTESI_GUN_IMSAK; // ertesi gun imsak
                relYatsi.setBackgroundColor(getResources().getColor(R.color.style_color_accent));

                PrayerTimes nextDayPrayerTimes = getNextDayPrayerTimes();

                if (nextDayPrayerTimes != null) {
                    Date prayerNextDayImsak = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getImsak());
                    targetDifference = prayerNextDayImsak.getTime() - currentDate.getTime();
                    nextPrayerTimeInMillis = prayerNextDayImsak.getTime();
                } else {
                    if (!downloadedFromServer) {
                        getPrayerTimesFromServer();
                    }
                }

            } else {
                target = PRAYER_TIME_IMSAK; //imsak
                relImsak.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer1.getTime() - currentDate.getTime();
                nextPrayerTimeInMillis = prayer1.getTime();
            }

            Config.notifTime = nextPrayerTimeInMillis;
            Config.updateNotif(getContext());
            //Log.i("notifTime", String.valueOf(Config.notifTime));
            //setNotif();
            setNotif2();

        } catch (ParseException e) {
            Log.e("parseError", e.toString());
        }

        return target;

    }

    private void setNotif2() {

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(Config.notifTime);

        NotifyMe.setNotifications(getContext());

        //NotifyMe.setNotif(getContext(), Config.notifTime);

        //Log.i("notifTime", String.valueOf(now));

        //NotifyMe.isValid(getContext(), 3);
        //NotifyMe.isValid(getContext(), 4);
        //NotifyMe.update(getContext());
        //NotifyMe.getAll(getContext(), "");
/*

        Intent intent = new Intent(getContext(), RepeatingActivity.class);
        intent.putExtra("test", "I am a String");
        NotifyMe notifyMe = new NotifyMe.Builder(getContext())
                .title("Başlık1")
                .content("İçerik 1")
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .addAction(intent, "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(intent, "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
*/

    }

    private PrayerTimes getNextDayPrayerTimes() {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDate = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String formattedDate = df.format(nextDate);

        for (int i = 0; i < prayerTimes.length; i++) {
            if (prayerTimes[i].getMiladiTarihKisa().equals(formattedDate)) {
                return prayerTimes[i];
            }
        }

        return null;

    }

    private void clearBackgrounds() {
        relImsak.setBackgroundColor(getResources().getColor(R.color.transparent));
        relGunes.setBackgroundColor(getResources().getColor(R.color.transparent));
        relOgle.setBackgroundColor(getResources().getColor(R.color.transparent));
        relIkindi.setBackgroundColor(getResources().getColor(R.color.transparent));
        relAksam.setBackgroundColor(getResources().getColor(R.color.transparent));
        relYatsi.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

}
