package com.uren.kuranezan.MainFragments.TabKuran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class KuranFragment extends BaseFragment {

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_kuran, container, false);
        ButterKnife.bind(this, view);

        setToolbar();

        return view;
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.quran));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
