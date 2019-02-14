package com.uren.kuranezan.MainFragments.TabImsakiye;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabImsakiye.SubFragments.BaseListFragment;
import com.uren.kuranezan.Models.QuranModels.Quran;
import com.uren.kuranezan.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_ULKE;

public class ImsakiyeFragment extends BaseFragment {

    View mView;
    //@BindView(R.id.txtToolbarTitle)
    //TextView txtToolbarTitle;
    @BindView(R.id.imgBackground)
    ImageView imgBackground;
    @BindView(R.id.imgDarkGradient)
    ImageView imgDarkGradient;

    @BindView(R.id.txtRemaining)
    TextView txtRemaining;

    @BindView(R.id.txtChangeLocation)
    TextView txtChangeLocation;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 60000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_imsakiye, container, false);
            ButterKnife.bind(this, mView);

            setToolbar();

            init();
            setUI();
            startTimer();

            setPrayerTimes();
        }

        return mView;
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
        txtChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentNavigation.pushFragment(BaseListFragment.newInstance(ITEM_TYPE_ULKE, 0));
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


    }

}
