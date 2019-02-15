package com.uren.kuranezan.MainFragments.TabDiger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.CumaMesajlariFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.DiniBilgiler.DiniBilgilerFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.KibleBulFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.ZikirmatikFragment;
import com.uren.kuranezan.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DigerFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    @BindView(R.id.llZikirmatik)
    LinearLayout llZikirmatik;
    @BindView(R.id.llKibleBul)
    LinearLayout llKibleBul;
    @BindView(R.id.llCumaMesajlari)
    LinearLayout llCumaMesajlari;
    @BindView(R.id.llDiniBilgiler)
    LinearLayout llDiniBilgiler;

    @BindView(R.id.imgZikirmatik)
    ImageView imgZikirmatik;
    @BindView(R.id.imgKibleBul)
    ImageView imgKibleBul;
    @BindView(R.id.imgCumaMesajlari)
    ImageView imgCumaMesajlari;
    @BindView(R.id.imgDiniBilgiler)
    ImageView imgDiniBilgiler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.VISIBLE);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_diger, container, false);
            ButterKnife.bind(this, mView);
            setToolbar();
            setImages();
            init();
        }

        return mView;
    }

    private void setImages() {
        Glide.with(getContext())
                .load(R.drawable.icon_zikirmatik)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgZikirmatik);

        Glide.with(getContext())
                .load(R.drawable.icon_kible_bul)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgKibleBul);

        Glide.with(getContext())
                .load(R.drawable.icon_cuma_mesajlari)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgCumaMesajlari);

        Glide.with(getContext())
                .load(R.drawable.dini_bilgiler)
                .apply(RequestOptions.fitCenterTransform())
                .into(imgDiniBilgiler);
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.more));
    }

    private void init() {
        llZikirmatik.setOnClickListener(this);
        llKibleBul.setOnClickListener(this);
        llCumaMesajlari.setOnClickListener(this);
        llDiniBilgiler.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == llZikirmatik){
            mFragmentNavigation.pushFragment(new ZikirmatikFragment());
        }

        if(view == llKibleBul){
            mFragmentNavigation.pushFragment(new KibleBulFragment());
        }

        if(view == llCumaMesajlari){
            mFragmentNavigation.pushFragment(new CumaMesajlariFragment());
        }

        if(view == llDiniBilgiler){
            mFragmentNavigation.pushFragment(new DiniBilgilerFragment());
        }
    }
}
