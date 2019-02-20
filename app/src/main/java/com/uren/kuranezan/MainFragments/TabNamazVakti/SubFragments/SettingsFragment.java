package com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.AsyncLangProcess;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.OptionsHelper;
import com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.DeviceBootReceiver;
import com.uren.kuranezan.MainFragments.TabNamazVakti.AlarmManagement.NotificationReceiver;
import com.uren.kuranezan.Models.QuranModels.Quran;
import com.uren.kuranezan.Models.TranslationModels.Data;
import com.uren.kuranezan.Models.TranslationModels.Translations;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.QuranTranslation;
import com.uren.kuranezan.Singleton.TranslationList;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.Config;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private int numberOfCallback;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgLeft)
    ImageView imgBack;
    //@BindView(R.id.adView)
    //AdView adView;

    @BindView(R.id.btnNotif)
    Button btnNotif;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, 0);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.GONE);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_notif_settings, container, false);
            ButterKnife.bind(this, mView);
            getItemsFromBundle();

            setToolbar();
            init();

        }

        return mView;
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            numberOfCallback = (Integer) args.getInt(ARGS_INSTANCE);
        }
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.options));
    }

    private void init() {
        //MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        //AdMobUtils.loadBannerAd(adView);
        //AdMobUtils.loadInterstitialAd(getContext());
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        btnNotif.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == btnNotif) {
            notifEnabledClicked();
        }

    }

    private void notifEnabledClicked() {

        Boolean dailyNotify = true;
        PackageManager pm = getContext().getPackageManager();
        //ComponentName receiver = new ComponentName(getContext(), DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
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
            /*
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
                    */
            //endregion
        } else { //Disable Daily Notifications
            if (PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0) != null && manager != null) {
                manager.cancel(pendingIntent);
                //Toast.makeText(this,"Notifications were disabled",Toast.LENGTH_SHORT).show();
            }
            /*
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
                    */
        }

    }


    /*******************************************************/


}
