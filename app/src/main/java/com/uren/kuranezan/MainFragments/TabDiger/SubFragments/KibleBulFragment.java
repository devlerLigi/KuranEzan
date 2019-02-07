package com.uren.kuranezan.MainFragments.TabDiger.SubFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class KibleBulFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgBack)
    ClickableImageView imgBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_kible_bul, container, false);
            ButterKnife.bind(this, mView);

            setToolbar();
            init();

        }


        return mView;
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.kibleBul));
    }


    private void init() {
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

    }
}
