package com.uren.kuranezan.MainFragments.TabDiger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.CumaMesajlariFragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_diger, container, false);
            ButterKnife.bind(this, mView);

            setToolbar();
            init();

        }


        return mView;
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.more));
    }


    private void init() {
        llZikirmatik.setOnClickListener(this);
        llKibleBul.setOnClickListener(this);
        llCumaMesajlari.setOnClickListener(this);
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

    }
}
