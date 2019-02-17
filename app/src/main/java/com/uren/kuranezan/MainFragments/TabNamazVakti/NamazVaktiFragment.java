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
import com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments.ImsakiyeFragment;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.PrayerTimesList;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;
import com.uren.kuranezan.Utils.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_ULKE;
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

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 60000;

    private PrayerTimes[] prayerTimes;
    private boolean downloadFromServer = false;

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

            setLocationInfo();
            getPrayerTimesFromLocal();
            startTimer();


        }

        return mView;
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.VISIBLE);
        super.onStart();
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

    private void init() {
        setRefreshListener();
        txtChangeLocation.setOnClickListener(this);
        txtImsakiye.setOnClickListener(this);
    }

    private void setLocationInfo() {
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
                    downloadFromServer = true;
                    Log.i("prayerTime ", "serverdan alindi");
                    setPrayerTimes();

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
            } else {
                Log.i("gunKontrolu", "lokaldeki dosyada geçerli gün yok!");
                if (!downloadFromServer) {
                    getPrayerTimesFromServer();
                }
            }

        }

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

    @Override
    public void onClick(View view) {

        if (view == txtChangeLocation) {
            changeLocationClicked();
        }

        if (view == txtImsakiye) {
            ImsakiyeClicked();
        }

    }

    private void changeLocationClicked() {
        String title = getString(R.string.country);
        mFragmentNavigation.pushFragment(BaseListFragment.newInstance(ITEM_TYPE_ULKE, 0, title));
    }

    private void ImsakiyeClicked() {
        if(PrayerTimesList.getInstance()!=null && PrayerTimesList.getInstance().getPrayerTimes()!= null){
            String title = getString(R.string.imsakiye);
            mFragmentNavigation.pushFragment(ImsakiyeFragment.newInstance(title));
        }
    }


}
