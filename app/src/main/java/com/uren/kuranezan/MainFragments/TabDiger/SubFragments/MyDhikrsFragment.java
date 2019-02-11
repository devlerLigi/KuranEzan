package com.uren.kuranezan.MainFragments.TabDiger.SubFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabDiger.Adapters.MyDhikrsAdapter;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.Zikirmatik.Interfaces.DhikrReturnCallback;
import com.uren.kuranezan.MainFragments.TabDiger.Utils.MyZikir;
import com.uren.kuranezan.MainFragments.TabDiger.Utils.TinyDB;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.StringConstants.MY_ZIKIRS;

@SuppressLint("ValidFragment")
public class MyDhikrsFragment extends BaseFragment {

    View mView;

    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imgLeft)
    ClickableImageView imgLeft;
    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    private MyDhikrsAdapter myDhikrsAdapter;
    private DhikrReturnCallback dhikrReturnCallback;

    public MyDhikrsFragment(DhikrReturnCallback dhikrReturnCallback) {
        this.dhikrReturnCallback = dhikrReturnCallback;
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
            mView = inflater.inflate(R.layout.fragment_my_dhikrs, container, false);
            ButterKnife.bind(this, mView);
            init();
            setShapes();
            addListeners();
            setDhikrAdapter();
        }

        return mView;
    }

    private void init() {
        imgLeft.setVisibility(View.VISIBLE);
        txtToolbarTitle.setText(getResources().getString(R.string.MY_DHIKR));
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
    }


    private void setShapes() {

    }

    private void addListeners() {
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setDhikrAdapter(){
        TinyDB tinyDB = new TinyDB(getContext());
        ArrayList<Object> tempList = tinyDB.getListObject(MY_ZIKIRS, MyZikir.class);

        myDhikrsAdapter = new MyDhikrsAdapter(getContext(), tempList, new DhikrReturnCallback() {
            @Override
            public void onReturn(MyZikir myZikir) {
                dhikrReturnCallback.onReturn(myZikir);
                getActivity().onBackPressed();
            }
        });
        recyclerView.setAdapter(myDhikrsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}
