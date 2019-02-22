package com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.NotifyMe;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.Config;


import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_AKSAM;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_GUNES;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IKINDI;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_OGLE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_YATSI;

public class SettingsFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgLeft)
    ImageView imgBack;
    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.llImsak)
    LinearLayout llImsak;
    @BindView(R.id.llGunes)
    LinearLayout llGunes;
    @BindView(R.id.llOgle)
    LinearLayout llOgle;
    @BindView(R.id.llIkindi)
    LinearLayout llIkindi;
    @BindView(R.id.llAksam)
    LinearLayout llAksam;
    @BindView(R.id.llYatsi)
    LinearLayout llYatsi;

    @BindView(R.id.txtImsakNotifDetail)
    TextView txtImsakNotifDetail;
    @BindView(R.id.txtGunesNotifDetail)
    TextView txtGunesNotifDetail;
    @BindView(R.id.txtOgleNotifDetail)
    TextView txtOgleNotifDetail;
    @BindView(R.id.txtIkindiNotifDetail)
    TextView txtIkindiNotifDetail;
    @BindView(R.id.txtAksamNotifDetail)
    TextView txtAksamNotifDetail;
    @BindView(R.id.txtYatsiNotifDetail)
    TextView txtYatsiNotifDetail;

    @BindView(R.id.txtNotifSituation)
    TextView txtNotifSituation;
    @BindView(R.id.switchNotificaitons)
    Switch switchNotificaitons;

    @BindView(R.id.llNotif)
    LinearLayout llNotif;


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

    @Override
    public void onResume() {
        super.onResume();
        setUI();
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.options));
    }

    private void setUI() {

        setNotifSituation();
        setSwitchListener();

    }

    private void setSwitchListener() {
        switchNotificaitons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Config.notifEnabled = true;
                    setNotifSituation();
                } else {
                    Config.notifEnabled = false;
                    setNotifSituation();
                }
                Config.updateNotif(getContext());
                Log.i("COMING_FROM", "SettingsFragment");
                NotifyMe.setNotifications(getContext());
            }
        });
    }

    private void setNotifSituation() {

        if (Config.notifEnabled) {
            switchNotificaitons.setChecked(true);
            txtNotifSituation.setText(getString(R.string.enabled));
            txtNotifSituation.setTextColor(getResources().getColor(R.color.style_color_accent));
            llNotif.setBackgroundColor(getResources().getColor(R.color.style_color_accent));

            txtImsakNotifDetail.setText(setNotifInfo(PRAYER_TIME_IMSAK));
            txtGunesNotifDetail.setText(setNotifInfo(PRAYER_TIME_GUNES));
            txtOgleNotifDetail.setText(setNotifInfo(PRAYER_TIME_OGLE));
            txtIkindiNotifDetail.setText(setNotifInfo(PRAYER_TIME_IKINDI));
            txtAksamNotifDetail.setText(setNotifInfo(PRAYER_TIME_AKSAM));
            txtYatsiNotifDetail.setText(setNotifInfo(PRAYER_TIME_YATSI));

        } else {
            switchNotificaitons.setChecked(false);
            txtNotifSituation.setText(getString(R.string.disabled));
            txtNotifSituation.setTextColor(getResources().getColor(R.color.red));
            llNotif.setBackgroundColor(getResources().getColor(R.color.red));

            txtImsakNotifDetail.setText(setNotifInfo(-1));
            txtGunesNotifDetail.setText(setNotifInfo(-1));
            txtOgleNotifDetail.setText(setNotifInfo(-1));
            txtIkindiNotifDetail.setText(setNotifInfo(-1));
            txtAksamNotifDetail.setText(setNotifInfo(-1));
            txtYatsiNotifDetail.setText(setNotifInfo(-1));
        }

        llImsak.setClickable(Config.notifEnabled);
        llGunes.setClickable(Config.notifEnabled);
        llOgle.setClickable(Config.notifEnabled);
        llIkindi.setClickable(Config.notifEnabled);
        llAksam.setClickable(Config.notifEnabled);
        llYatsi.setClickable(Config.notifEnabled);

    }


    private String setNotifInfo(int request) {

        String val1 = getString(R.string.disabled);
        String val2 = getString(R.string.disabled);

        if (request == PRAYER_TIME_IMSAK) {
            if (Config.notifBeforeImsak) {
                val1 = String.valueOf(Config.timeBeforeImsak) + " " + getString(R.string.minutes);
            }
            if (Config.notifExactImsak) {
                val2 = getString(R.string.enabled);
            }
        }

        if (request == PRAYER_TIME_GUNES) {
            if (Config.notifBeforeGunes) {
                val1 = String.valueOf(Config.timeBeforeAksam) + " " + getString(R.string.minutes);
            }
            if (Config.notifExactGunes) {
                val2 = getString(R.string.enabled);
            }
        }

        if (request == PRAYER_TIME_OGLE) {
            if (Config.notifBeforeOgle) {
                val1 = String.valueOf(Config.timeBeforeOgle) + " " + getString(R.string.minutes);
            }
            if (Config.notifExactOgle) {
                val2 = getString(R.string.enabled);
            }
        }

        if (request == PRAYER_TIME_IKINDI) {
            if (Config.notifBeforeIkindi) {
                val1 = String.valueOf(Config.timeBeforeIkindi) + " " + getString(R.string.minutes);
            }
            if (Config.notifExactIkindi) {
                val2 = getString(R.string.enabled);
            }
        }

        if (request == PRAYER_TIME_AKSAM) {
            if (Config.notifBeforeAksam) {
                val1 = String.valueOf(Config.timeBeforeAksam) + " " + getString(R.string.minutes);
            }
            if (Config.notifExactAksam) {
                val2 = getString(R.string.enabled);
            }
        }

        if (request == PRAYER_TIME_YATSI) {
            if (Config.notifBeforeYatsi) {
                val1 = String.valueOf(Config.timeBeforeYatsi) + " " + getString(R.string.minutes);
            }
            if (Config.notifExactYatsi) {
                val2 = getString(R.string.enabled);
            }
        }

        return getString(R.string.before) + " " + val1 + " " + getString(R.string.seperator) + " " + getString(R.string.exactime) + " " + val2;

    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        AdMobUtils.loadInterstitialAd(getContext());
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);

        llImsak.setOnClickListener(this);
        llGunes.setOnClickListener(this);
        llOgle.setOnClickListener(this);
        llIkindi.setOnClickListener(this);
        llAksam.setOnClickListener(this);
        llYatsi.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == llImsak) {
            int prayerTime = PRAYER_TIME_IMSAK;
            mFragmentNavigation.pushFragment(NotifDetailFragment.newInstance(prayerTime));
        }

        if (view == llGunes) {
            int prayerTime = PRAYER_TIME_GUNES;
            mFragmentNavigation.pushFragment(NotifDetailFragment.newInstance(prayerTime));
        }

        if (view == llOgle) {
            int prayerTime = PRAYER_TIME_OGLE;
            mFragmentNavigation.pushFragment(NotifDetailFragment.newInstance(prayerTime));
        }

        if (view == llIkindi) {
            int prayerTime = PRAYER_TIME_IKINDI;
            mFragmentNavigation.pushFragment(NotifDetailFragment.newInstance(prayerTime));
        }

        if (view == llAksam) {
            int prayerTime = PRAYER_TIME_AKSAM;
            mFragmentNavigation.pushFragment(NotifDetailFragment.newInstance(prayerTime));
        }

        if (view == llYatsi) {
            int prayerTime = PRAYER_TIME_YATSI;
            mFragmentNavigation.pushFragment(NotifDetailFragment.newInstance(prayerTime));
        }

    }


    /*******************************************************/


}
