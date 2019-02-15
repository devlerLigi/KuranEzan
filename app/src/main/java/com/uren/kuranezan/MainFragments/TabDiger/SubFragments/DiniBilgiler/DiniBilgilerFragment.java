package com.uren.kuranezan.MainFragments.TabDiger.SubFragments.DiniBilgiler;


import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.CumaMesajlari.CumaMesajiPhotoFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.CumaMesajlari.CumaMesajlariContent;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.CumaMesajlariFragment;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.BitmapConversion;
import com.uren.kuranezan.Utils.ShapeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.StringConstants.file_ibadet_nedir;

public class DiniBilgilerFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;

    @BindView(R.id.imgLeft)
    ImageView imgLeft;
    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    @BindView(R.id.llibadet)
    LinearLayout llibadet;
    @BindView(R.id.llImaninSartlari)
    LinearLayout llImaninSartlari;
    @BindView(R.id.llIslaminSartlari)
    LinearLayout llIslaminSartlari;
    @BindView(R.id.ll32farz)
    LinearLayout ll32farz;
    @BindView(R.id.ll54Farz)
    LinearLayout ll54Farz;
    @BindView(R.id.llFarzNedir)
    LinearLayout llFarzNedir;
    @BindView(R.id.llSunnetNedir)
    LinearLayout llSunnetNedir;
    @BindView(R.id.llVacipNedir)
    LinearLayout llVacipNedir;
    @BindView(R.id.llKelimeiSahadet)
    LinearLayout llKelimeiSahadet;
    @BindView(R.id.llEzan)
    LinearLayout llEzan;

    @BindView(R.id.llkelimeiTevhid)
    LinearLayout llkelimeiTevhid;
    @BindView(R.id.llNamazRekatlari)
    LinearLayout llNamazRekatlari;
    @BindView(R.id.llAbdestNasilAlinir)
    LinearLayout llAbdestNasilAlinir;
    @BindView(R.id.llErkeklerdeNamaz)
    LinearLayout llErkeklerdeNamaz;
    @BindView(R.id.llKadinlarNamaz)
    LinearLayout llKadinlarNamaz;

    public DiniBilgilerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_dini_bilgiler, container, false);
            ButterKnife.bind(this, mView);
            setToolbar();
            init();
        }

        return mView;
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.GONE);
        super.onStart();
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.diniBilgiler));
    }

    private void init() {
        imgLeft.setVisibility(View.VISIBLE);
        llibadet.setOnClickListener(this);
        llImaninSartlari.setOnClickListener(this);
        llIslaminSartlari.setOnClickListener(this);
        ll32farz.setOnClickListener(this);
        ll54Farz.setOnClickListener(this);
        llFarzNedir.setOnClickListener(this);
        llSunnetNedir.setOnClickListener(this);
        llVacipNedir.setOnClickListener(this);
        llKelimeiSahadet.setOnClickListener(this);
        llEzan.setOnClickListener(this);
        llkelimeiTevhid.setOnClickListener(this);
        llNamazRekatlari.setOnClickListener(this);
        llAbdestNasilAlinir.setOnClickListener(this);
        llErkeklerdeNamaz.setOnClickListener(this);
        llKadinlarNamaz.setOnClickListener(this);
        imgLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == imgLeft) {
            getActivity().onBackPressed();
        }
        if (view == llibadet) {
            startBilgiContentFragment(getResources().getString(R.string.IBADET_NEDIR),
                    R.raw.ibadet_nedir);
        }
        if (view == llImaninSartlari) {
            startBilgiContentFragment(getResources().getString(R.string.IMANIN_SARTLARI),
                    R.raw.imanin_sartlari);
        }
        if (view == llIslaminSartlari) {
            startBilgiContentFragment(getResources().getString(R.string.ISLAMIN_SARTLARI),
                    R.raw.islamin_sartlari);
        }
        if (view == ll32farz) {
            startBilgiContentFragment(getResources().getString(R.string.OTUZIKI_FARZ),
                    R.raw.otuziki_farz);
        }
        if (view == ll54Farz) {
            startBilgiContentFragment(getResources().getString(R.string.ELLIDORT_FARZ),
                    R.raw.ellidort_farz);
        }
        if (view == llFarzNedir) {
            startBilgiContentFragment(getResources().getString(R.string.FARZ_NEDIR),
                    R.raw.farz_nedir);
        }
        if (view == llSunnetNedir) {
            startBilgiContentFragment(getResources().getString(R.string.SUNNET_NEDIR),
                    R.raw.sunnet_nedir);
        }
        if (view == llVacipNedir) {
            startBilgiContentFragment(getResources().getString(R.string.VACIP_NEDIR),
                    R.raw.vacip_nedir);
        }
        if (view == llKelimeiSahadet) {
            startBilgiContentFragment(getResources().getString(R.string.KELIMEI_SAHADET),
                    R.raw.kelimei_sahadet);
        }
        if (view == llEzan) {
            startBilgiContentFragment(getResources().getString(R.string.EZAN),
                    R.raw.ezan);
        }

        if (view == llkelimeiTevhid) {
            startBilgiContentFragment(getResources().getString(R.string.KELIMEI_TEVHID),
                    R.raw.kelimei_tevhid);
        }

        if (view == llNamazRekatlari) {
            startBilgiContentFragment(getResources().getString(R.string.NAMAZ_REKATLARI),
                    R.raw.namaz_rekatlari);
        }

        if (view == llAbdestNasilAlinir) {
            startBilgiContentFragment(getResources().getString(R.string.ABDEST_NASIL_ALINIR),
                    R.raw.abdest_nasil_alinir);
        }

        if (view == llErkeklerdeNamaz) {
            startBilgiContentFragment(getResources().getString(R.string.ERKEKLER_NAMAZ_NASIL),
                    R.raw.erkeklerde_namaz);
        }

        if (view == llKadinlarNamaz) {
            startBilgiContentFragment(getResources().getString(R.string.KADINLAR_NAMAZ_NASIL),
                    R.raw.kadinlarda_namaz);
        }
    }

    private void startBilgiContentFragment(String title, int rawItem) {
        mFragmentNavigation.pushFragment(new DiniBilgilerContentFragment(title, rawItem));
    }
}