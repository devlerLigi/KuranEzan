package com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabNamazVakti.Notify.NotifyMe;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.Config;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_AKSAM;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_GUNES;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IKINDI;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_IMSAK;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_OGLE;
import static com.uren.kuranezan.Constants.NumericConstants.PRAYER_TIME_YATSI;

public class NotifDetailFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private int prayerTime;
    private int timeBefore;
    private int selectedBeforeMelody, selectedExactMelody;
    private MediaPlayer mediaPlayer;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgLeft)
    ImageView imgBack;
    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.txtBeforeNotifTime)
    TextView txtBeforeNotifTime;
    @BindView(R.id.txtBeforeNotifMelody)
    TextView txtBeforeNotifMelody;
    @BindView(R.id.txtExactNotifMelody)
    TextView txtExactNotifMelody;

    @BindView(R.id.txtHeaderBefore)
    TextView txtHeaderBefore;
    @BindView(R.id.txtHeaderExact)
    TextView txtHeaderExact;

    @BindView(R.id.llNotifTime1)
    LinearLayout llNotifTime1;
    @BindView(R.id.llNotifyMelody1)
    LinearLayout llNotifyMelody1;
    @BindView(R.id.llNotifyMelody2)
    LinearLayout llNotifyMelody2;

    @BindView(R.id.switchBeforeTime)
    Switch switchBeforeTime;
    @BindView(R.id.switchExactTime)
    Switch switchExactTime;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btnApply)
    Button btnApply;

    @BindArray(R.array.sounds)
    String[] melodies;


    private static final int DEFAULT_INTERVAL_TIME = 5;
    private static final int MAX_INTERVAL_COUNTS = 12;

    public static NotifDetailFragment newInstance(int prayerTime) {
        Bundle args = new Bundle();
        args.putInt("prayerTime", prayerTime);
        NotifDetailFragment fragment = new NotifDetailFragment();
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
            mView = inflater.inflate(R.layout.fragment_notif_detail, container, false);
            ButterKnife.bind(this, mView);
            getItemsFromBundle();

            setToolbar();
            setUI();
            init();

        }

        return mView;
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            prayerTime = args.getInt("prayerTime", prayerTime);
        }
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.adjustNotificationDetail));
    }

    private void setUI() {

        String[] prayerTimeNames;
        prayerTimeNames = getResources().getStringArray(R.array.prayerTimes);

        String heaederBefore = prayerTimeNames[prayerTime - 1] + " " + getResources().getString(R.string.headerTextBefore);
        String headerExact = prayerTimeNames[prayerTime - 1] + " " + getResources().getString(R.string.headerTextExact);

        txtHeaderBefore.setText(heaederBefore);
        txtHeaderExact.setText(headerExact);


        setCurrentSituationOnTV();

    }

    private void setCurrentSituationOnTV() {

        if (prayerTime == PRAYER_TIME_IMSAK) {
            timeBefore = Config.timeBeforeImsak;
            selectedBeforeMelody = Config.melodyBeforeImsak;
            selectedExactMelody = Config.melodyImsak;
            switchBeforeTime.setChecked(Config.notifBeforeImsak);
            switchExactTime.setChecked(Config.notifExactImsak);
            txtBeforeNotifTime.setText(String.valueOf(Config.timeBeforeImsak) + " " + getString(R.string.minutes));
            txtBeforeNotifMelody.setText(melodies[Config.melodyBeforeImsak]);
            txtExactNotifMelody.setText(melodies[Config.melodyImsak]);
        }

        if (prayerTime == PRAYER_TIME_GUNES) {
            timeBefore = Config.timeBeforeGunes;
            selectedBeforeMelody = Config.melodyBeforeGunes;
            selectedExactMelody = Config.melodyGunes;
            switchBeforeTime.setChecked(Config.notifBeforeGunes);
            switchExactTime.setChecked(Config.notifExactGunes);
            txtBeforeNotifTime.setText(String.valueOf(Config.timeBeforeGunes) + " " + getString(R.string.minutes));
            txtBeforeNotifMelody.setText(melodies[Config.melodyBeforeGunes]);
            txtExactNotifMelody.setText(melodies[Config.melodyGunes]);
        }

        if (prayerTime == PRAYER_TIME_OGLE) {
            timeBefore = Config.timeBeforeOgle;
            selectedBeforeMelody = Config.melodyBeforeOgle;
            selectedExactMelody = Config.melodyOgle;
            switchBeforeTime.setChecked(Config.notifBeforeOgle);
            switchExactTime.setChecked(Config.notifExactOgle);
            txtBeforeNotifTime.setText(String.valueOf(Config.timeBeforeOgle) + " " + getString(R.string.minutes));
            txtBeforeNotifMelody.setText(melodies[Config.melodyBeforeOgle]);
            txtExactNotifMelody.setText(melodies[Config.melodyOgle]);
        }

        if (prayerTime == PRAYER_TIME_IKINDI) {
            timeBefore = Config.timeBeforeIkindi;
            selectedBeforeMelody = Config.melodyBeforeIkindi;
            selectedExactMelody = Config.melodyIkindi;
            switchBeforeTime.setChecked(Config.notifBeforeIkindi);
            switchExactTime.setChecked(Config.notifExactIkindi);
            txtBeforeNotifTime.setText(String.valueOf(Config.timeBeforeIkindi) + " " + getString(R.string.minutes));
            txtBeforeNotifMelody.setText(melodies[Config.melodyBeforeIkindi]);
            txtExactNotifMelody.setText(melodies[Config.melodyIkindi]);
        }

        if (prayerTime == PRAYER_TIME_AKSAM) {
            timeBefore = Config.timeBeforeAksam;
            selectedBeforeMelody = Config.melodyBeforeAksam;
            selectedExactMelody = Config.melodyAksam;
            switchBeforeTime.setChecked(Config.notifBeforeAksam);
            switchExactTime.setChecked(Config.notifExactAksam);
            txtBeforeNotifTime.setText(String.valueOf(Config.timeBeforeAksam) + " " + getString(R.string.minutes));
            txtBeforeNotifMelody.setText(melodies[Config.melodyBeforeAksam]);
            txtExactNotifMelody.setText(melodies[Config.melodyAksam]);
        }

        if (prayerTime == PRAYER_TIME_YATSI) {
            timeBefore = Config.timeBeforeYatsi;
            selectedBeforeMelody = Config.melodyBeforeYatsi;
            selectedExactMelody = Config.melodyYatsi;
            switchBeforeTime.setChecked(Config.notifBeforeYatsi);
            switchExactTime.setChecked(Config.notifExactYatsi);
            txtBeforeNotifTime.setText(String.valueOf(Config.timeBeforeYatsi) + " " + getString(R.string.minutes));
            txtBeforeNotifMelody.setText(melodies[Config.melodyBeforeYatsi]);
            txtExactNotifMelody.setText(melodies[Config.melodyYatsi]);
        }

    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        AdMobUtils.loadInterstitialAd(getContext());
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);

        llNotifTime1.setOnClickListener(this);
        llNotifyMelody1.setOnClickListener(this);
        llNotifyMelody2.setOnClickListener(this);
        btnApply.setOnClickListener(this);

        setSwitchListeners();
    }

    private void setSwitchListeners() {

        switchBeforeTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                applyChanges();
            }
        });

        switchExactTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                applyChanges();
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == llNotifTime1) {
            notifTimeBeforeClicked();
        }


        if (view == llNotifyMelody1) {
            notifMelodyClicked(1);
        }


        if (view == llNotifyMelody2) {
            notifMelodyClicked(2);
        }

        if (view == btnApply) {
            applyChanges();
            //getActivity().onBackPressed();
        }

    }

    private void notifTimeBeforeClicked() {
        List<Integer> timesList = getTimes();
        showRadioDialogBox(timesList);

    }


    private void notifMelodyClicked(int i) {
        showRadioGroupForMelodies(i);
    }

    private void applyChanges() {
        progressBar.setVisibility(View.VISIBLE);
        updateConfig();
        NotifyMe.setNotifications(getContext());
        progressBar.setVisibility(View.GONE);
    }

    private void updateConfig() {

        if (prayerTime == PRAYER_TIME_IMSAK) {
            Config.notifBeforeImsak = switchBeforeTime.isChecked();
            Config.notifExactImsak = switchExactTime.isChecked();
            Config.timeBeforeImsak = timeBefore;
            Config.melodyBeforeImsak = selectedBeforeMelody;
            Config.melodyImsak = selectedExactMelody;
        }

        if (prayerTime == PRAYER_TIME_GUNES) {
            Config.notifBeforeGunes = switchBeforeTime.isChecked();
            Config.notifExactGunes = switchExactTime.isChecked();
            Config.timeBeforeGunes = timeBefore;
            Config.melodyBeforeGunes = selectedBeforeMelody;
            Config.melodyGunes = selectedExactMelody;
        }

        if (prayerTime == PRAYER_TIME_OGLE) {
            Config.notifBeforeOgle = switchBeforeTime.isChecked();
            Config.notifExactOgle = switchExactTime.isChecked();
            Config.timeBeforeOgle = timeBefore;
            Config.melodyBeforeOgle = selectedBeforeMelody;
            Config.melodyOgle = selectedExactMelody;
        }

        if (prayerTime == PRAYER_TIME_IKINDI) {
            Config.notifBeforeIkindi = switchBeforeTime.isChecked();
            Config.notifExactIkindi = switchExactTime.isChecked();
            Config.timeBeforeIkindi = timeBefore;
            Config.melodyBeforeIkindi = selectedBeforeMelody;
            Config.melodyIkindi = selectedExactMelody;
        }

        if (prayerTime == PRAYER_TIME_AKSAM) {
            Config.notifBeforeAksam = switchBeforeTime.isChecked();
            Config.notifExactAksam = switchExactTime.isChecked();
            Config.timeBeforeAksam = timeBefore;
            Config.melodyBeforeAksam = selectedBeforeMelody;
            Config.melodyAksam = selectedExactMelody;
        }

        if (prayerTime == PRAYER_TIME_YATSI) {
            Config.notifBeforeYatsi = switchBeforeTime.isChecked();
            Config.notifExactYatsi = switchExactTime.isChecked();
            Config.timeBeforeYatsi = timeBefore;
            Config.melodyBeforeYatsi = selectedBeforeMelody;
            Config.melodyYatsi = selectedExactMelody;
        }

        Config.updateNotif(getContext());

    }


    /*******************************************************/

    private List<Integer> getTimes() {

        List<Integer> timesList = new ArrayList<>();
        for (int i = 1; i <= MAX_INTERVAL_COUNTS; i++) {
            timesList.add(i * DEFAULT_INTERVAL_TIME);
        }

        return timesList;
    }

    private void showRadioDialogBox(final List<Integer> timesList) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.radiobutton_dialog);
        ScrollView scrollView = (ScrollView) dialog.findViewById(R.id.scrollView);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnApply = (Button) dialog.findViewById(R.id.btnApply);

        for (int i = 0; i < timesList.size(); i++) {

            int time = timesList.get(i);
            String text = String.valueOf(time) + " " + getString(R.string.minutes);
            RadioButton rb = new RadioButton(getContext());
            rb.setText(text);
            rb.setId(i + 1);

            rg.addView(rb);

            if (time == timeBefore) {
                rb.setChecked(true);
            }

        }

        dialog.show();

        //set Listeners
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBefore = rg.getCheckedRadioButtonId() * DEFAULT_INTERVAL_TIME;
                String text = String.valueOf(timeBefore) + " " + getString(R.string.minutes);
                txtBeforeNotifTime.setText(text);
                dialog.dismiss();

                applyChanges();
            }
        });


    }

    private void showRadioGroupForMelodies(final int selectedView) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.radiobutton_dialog);
        ScrollView scrollView = (ScrollView) dialog.findViewById(R.id.scrollView);
        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnApply = (Button) dialog.findViewById(R.id.btnApply);

        for (int i = 0; i < melodies.length; i++) {

            String melody = melodies[i];
            RadioButton rb = new RadioButton(getContext());
            rb.setText(melody);
            rb.setId(i);

            rg.addView(rb);

            if (selectedView == 1) {
                if (i == selectedBeforeMelody) {
                    rb.setChecked(true);
                }
            } else if (selectedView == 2) {
                if (i == selectedExactMelody) {
                    rb.setChecked(true);
                }
            }

        }

        dialog.show();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int index = rg.getCheckedRadioButtonId();

                if (mediaPlayer != null) {
                    mediaPlayer.reset();// stops any current playing song
                }
                if (index != 0) {
                    mediaPlayer = MediaPlayer.create(getContext(), NotifyMe.getRawItem(index));
                    mediaPlayer.start();
                }

            }
        });

        //set Listeners
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (mediaPlayer != null) {
                    mediaPlayer.reset();// stops any current playing song
                }

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selectedView == 1) {
                    selectedBeforeMelody = rg.getCheckedRadioButtonId();
                    txtBeforeNotifMelody.setText(melodies[selectedBeforeMelody]);
                } else if (selectedView == 2) {
                    selectedExactMelody = rg.getCheckedRadioButtonId();
                    txtExactNotifMelody.setText(melodies[selectedExactMelody]);
                }

                dialog.dismiss();
                if (mediaPlayer != null) {
                    mediaPlayer.reset();// stops any current playing song
                }

                applyChanges();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

    }
}
