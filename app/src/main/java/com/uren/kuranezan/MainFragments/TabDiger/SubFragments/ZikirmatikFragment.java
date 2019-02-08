package com.uren.kuranezan.MainFragments.TabDiger.SubFragments;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;
import com.uren.kuranezan.Utils.ShapeUtil;
import com.uren.kuranezan.Utils.ToastMessageUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ZikirmatikFragment extends BaseFragment {

    View mView;

    @BindView(R.id.vibrationImgv)
    ImageView vibrationImgv;
    @BindView(R.id.nightModeImgv)
    ImageView nightModeImgv;
    @BindView(R.id.themeImgv)
    ImageView themeImgv;
    @BindView(R.id.saveBtn)
    Button saveBtn;
    @BindView(R.id.zikirlerimBtn)
    Button zikirlerimBtn;
    @BindView(R.id.btnZikir)
    View btnZikir;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.mainFragLayout)
    FrameLayout mainFragLayout;

    boolean vibrationEnabled = false;
    boolean nightModeEnabled = false;

    private int currentBgColor = R.color.MediumSeaGreen;

    int colorList[] = {
            R.color.green,
            R.color.DodgerBlue,
            R.color.Orange,
            R.color.Red,
            R.color.Yellow,
            R.color.MediumSeaGreen,
            R.color.LightBlue,
            R.color.Sienna,
            R.color.SeaGreen,
            R.color.RoyalBlue,
            R.color.SandyBrown
    };

    public ZikirmatikFragment() {

    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.GONE);
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_zikirmatik, container, false);
            ButterKnife.bind(this, mView);
            init();
            setShapes();
            addListeners();
        }

        return mView;
    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
    }


    private void setShapes() {
        vibrationImgv.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.OVAL, 50, 0));
        nightModeImgv.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.OVAL, 50, 0));
        themeImgv.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.OVAL, 50, 0));
        saveBtn.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.RECTANGLE, 25, 0));
        zikirlerimBtn.setBackground(ShapeUtil.getShape(getResources().getColor(R.color.black_25_transparent),
                0, GradientDrawable.RECTANGLE, 25, 0));
    }

    private void addListeners() {
        vibrationImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibrationEnabled)
                    vibrationEnabled = false;
                else
                    vibrationEnabled = true;

                setVibrationImage();
            }
        });

        nightModeImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightModeEnabled)
                    nightModeEnabled = false;
                else
                    nightModeEnabled = true;

                setNightMode();
            }
        });

        themeImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nightModeEnabled){
                    ToastMessageUtil.showToastShort(getContext(), getContext().getResources().getString(R.string.NIGHT_MODE_MESSAGE));
                    return;
                }
                changeBackgroundColor();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        zikirlerimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnZikir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVibration();
            }
        });
    }

    private void setVibrationImage() {
        if (vibrationEnabled) {
            Glide.with(getContext())
                    .load(R.drawable.icon_vibration)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(vibrationImgv);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.icon_smartphone)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(vibrationImgv);
        }
    }

    private void setNightMode() {
        if (nightModeEnabled) {
            Glide.with(getContext())
                    .load(R.drawable.icon_night_mode_on).apply(RequestOptions.fitCenterTransform()).into(nightModeImgv);
            mainFragLayout.setBackgroundColor(getResources().getColor(R.color.Gray));
        } else {
            Glide.with(getContext())
                    .load(R.drawable.icon_night_mode_off).apply(RequestOptions.fitCenterTransform()).into(nightModeImgv);
            mainFragLayout.setBackgroundColor(getResources().getColor(currentBgColor));
        }
    }

    private void checkVibration() {
        if (vibrationEnabled) {
            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
        }
    }

    private void changeBackgroundColor(){
        Random rand = new Random();

        int randColor = colorList[rand.nextInt(colorList.length)];

        if(randColor == currentBgColor)
            changeBackgroundColor();

        currentBgColor = randColor;
        mainFragLayout.setBackgroundColor(getResources().getColor(currentBgColor));
    }

}
