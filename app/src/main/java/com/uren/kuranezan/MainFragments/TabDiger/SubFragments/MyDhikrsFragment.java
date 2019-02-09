package com.uren.kuranezan.MainFragments.TabDiger.SubFragments;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.DialogBoxUtil.DialogBoxUtil;
import com.uren.kuranezan.Utils.DialogBoxUtil.Interfaces.YesNoDialogBoxCallback;
import com.uren.kuranezan.Utils.ShapeUtil;
import com.uren.kuranezan.Utils.ToastMessageUtil;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyDhikrsFragment extends BaseFragment {

    View mView;

    @BindView(R.id.adView)
    AdView adView;

    public MyDhikrsFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

    }

    private void addListeners() {

    }

}
