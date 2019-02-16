package com.uren.kuranezan.MainFragments.TabNamazVakti;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uren.kuranezan.Interfaces.CompleteCallback;
import com.uren.kuranezan.Interfaces.NamazVaktiCallback;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses.NamazHelper;
import com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments.BaseListFragment;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.PrayerTimesList;
import com.uren.kuranezan.Utils.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_ULKE;

public class NamazVaktiFragment extends BaseFragment {

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

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 60000;

    private PrayerTimes[] prayerTimes;

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

            setToolbar();

            init();
            setUI();
            startTimer();

            setLocationInfo();
            getPrayerTimes();

        }

        return mView;
    }

    private void getPrayerTimes() {
        PrayerTimesList.getInstance(getActivity(), new CompleteCallback<PrayerTimes[]>() {
            @Override
            public void onComplete(PrayerTimes[] p) {
                prayerTimes = p;
                //setPrayerTimes();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }


    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.VISIBLE);
        super.onStart();
    }

    private void setToolbar() {
        //txtToolbarTitle.setText(getString(R.string.prayer));
    }

    private void init() {

        setRefreshListener();

        txtChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = getString(R.string.country);
                mFragmentNavigation.pushFragment(BaseListFragment.newInstance(ITEM_TYPE_ULKE, 0, title));
            }
        });
    }

    private void setRefreshListener() {

        NamazHelper.NamazVaktiRefresh.getInstance().setNamazVaktiCallback(new NamazVaktiCallback() {
            @Override
            public void onNamazVaktiRefresh() {

                if (PrayerTimesList.getInstance().getPrayerTimes() != null) {
                    setLocationInfo();
                    setPrayerTimes();
                }

            }


        });
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

    private void startTimer() {

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }


            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                startTimer();
                Log.i("info", "Timer start again");
            }
        }.start();

        mTimerRunning = true;

    }

    private void updateCountDownText() {

        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        txtRemaining.setText(timeLeftFormatted);

    }


    private void setPrayerTimes() {

        PrayerTimesList instance = PrayerTimesList.getInstance();

        if (instance != null && instance.getPrayerTimes() != null) {
            prayerTimes = instance.getPrayerTimes();

            PrayerTimes currentDate = getCurrentPrayerTime();

            txtImsakTime.setText(currentDate.getImsak());
            txtGunesTime.setText(currentDate.getGunes());
            txtOgleTime.setText(currentDate.getOgle());
            txtIkindiTime.setText(currentDate.getIkindi());
            txtAksamTime.setText(currentDate.getAksam());
            txtYatsiTime.setText(currentDate.getYatsi());

        }

    }

    private void setLocationInfo() {
        txtCountry.setText(Config.country);
        txtCity.setText(Config.city);
        txtCounty.setText(Config.county);
    }

    private PrayerTimes getCurrentPrayerTime() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c);

        for (int i = 0; i < prayerTimes.length; i++) {
            if (prayerTimes[i].getMiladiTarihKisa().equals(formattedDate)) {
                return prayerTimes[i];
            }
        }

        return null;
    }

}
