package com.uren.kuranezan.MainFragments.TabNamazVakti;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uren.kuranezan.Interfaces.CompleteCallback;
import com.uren.kuranezan.Interfaces.NamazVaktiCallback;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.NotifyMe;
import com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses.NamazHelper;
import com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments.BaseListFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments.ImsakiyeFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments.SettingsFragment;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.PrayerTimesList;
import com.uren.kuranezan.Utils.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    }

    private int getNextPrayerTime(PrayerTimes currentPrayerTime) {

        int target = 0;

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

            } else if (currentDate.compareTo(prayer2) > 0 && currentDate.compareTo(prayer3) <= 0) {
                target = PRAYER_TIME_OGLE; // ogle
                relOgle.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer3.getTime() - currentDate.getTime();
            } else if (currentDate.compareTo(prayer3) > 0 && currentDate.compareTo(prayer4) <= 0) {
                target = PRAYER_TIME_IKINDI; //ikindi
                relIkindi.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer4.getTime() - currentDate.getTime();
            } else if (currentDate.compareTo(prayer4) > 0 && currentDate.compareTo(prayer5) <= 0) {
                target = PRAYER_TIME_AKSAM; //aksam
                relAksam.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer5.getTime() - currentDate.getTime();
            } else if (currentDate.compareTo(prayer5) > 0 && currentDate.compareTo(prayer6) <= 0) {
                target = PRAYER_TIME_YATSI; //yatsi
                relYatsi.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer6.getTime() - currentDate.getTime();
            } else if (currentDate.compareTo(prayer6) > 0) {
                target = PRAYER_TIME_ERTESI_GUN_IMSAK; // ertesi gun imsak
                relYatsi.setBackgroundColor(getResources().getColor(R.color.style_color_accent));

                PrayerTimes nextDayPrayerTimes = getNextDayPrayerTimes();

                if (nextDayPrayerTimes != null) {
                    Date prayerNextDayImsak = dateFormat.parse(nextDayPrayerTimes.getMiladiTarihKisa() + " " + nextDayPrayerTimes.getImsak());
                    targetDifference = prayerNextDayImsak.getTime() - currentDate.getTime();
                } else {
                    if (!downloadedFromServer) {
                        getPrayerTimesFromServer();
                    }
                }

            } else {
                target = PRAYER_TIME_IMSAK; //imsak
                relImsak.setBackgroundColor(getResources().getColor(R.color.style_color_accent));
                targetDifference = prayer1.getTime() - currentDate.getTime();
            }

            Config.updateNotif(getContext());
            setNotif();

        } catch (ParseException e) {
            Log.e("parseError", e.toString());
        }

        return target;

    }

    private void setNotif() {
        NotifyMe.setNotifications(getContext());
        //NotifyMe.setDummyNotif(getContext());
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
