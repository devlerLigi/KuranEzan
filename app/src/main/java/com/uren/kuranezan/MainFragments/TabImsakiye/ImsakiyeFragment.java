package com.uren.kuranezan.MainFragments.TabImsakiye;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImsakiyeFragment extends BaseFragment {

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_imsakiye, container, false);
        ButterKnife.bind(this, view);

        setToolbar();

        getPrayerTimes();

        return view;
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.VISIBLE);
        super.onStart();
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.prayer));
    }

    private void getPrayerTimes() {

        double lat = 40.568459;
        double lng = -89.643028;


        try {
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {

                System.out.println(addresses.get(0).getLocality());
            } else {
                // do your stuff
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
