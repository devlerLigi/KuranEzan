package com.uren.kuranezan.MainFragments.TabDiger.SubFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.Interfaces.CompassListener;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.KibleBul.Compass;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.KibleBul.GPSTracker;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.INVISIBLE;


public class KibleBulFragment extends BaseFragment {

    View mView;
    private Compass compass;

    @BindView(R.id.main_image_qiblat)
    ImageView arrowViewQiblat;
    @BindView(R.id.main_image_dial)
    ImageView imageDial;
    @BindView(R.id.locDescTv)
    TextView locDescTv;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.imgLeft)
    ImageView imgLeft;
    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;

    public Menu menu;
    private float currentAzimuth;
    SharedPreferences prefs;
    GPSTracker gps;

    private static final String QIBLE_DIRECTION  = "QIBLE_DIRECTION";

    // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
    private static final double QIBLE_LONGITUDE = 39.826206;

    // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
    private static final double QIBLE_LATITUDE = 21.422487;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_kible_bul, container, false);
            ButterKnife.bind(this, mView);
            setListeners();
            setImages();
            init();
        }

        return mView;
    }

    private void init() {
        imgLeft.setVisibility(View.VISIBLE);
        txtToolbarTitle.setText(getString(R.string.kibleBul));
        MobileAds.initialize(getContext(), getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        prefs = getContext().getSharedPreferences("", MODE_PRIVATE);
        gps = new GPSTracker(getContext());
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        arrowViewQiblat.setVisibility(INVISIBLE);
        arrowViewQiblat.setVisibility(View.GONE);
        checkPermission();
    }

    private void setImages(){
        Glide.with(getContext())
                .load(R.drawable.dial)
                .apply(RequestOptions.fitCenterTransform())
                .into(imageDial);
        Glide.with(getContext())
                .load(R.drawable.jarum_qiblat)
                .apply(RequestOptions.fitCenterTransform())
                .into(arrowViewQiblat);
    }

    private void setListeners(){
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.GONE);
        if (compass != null) {
            compass.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (compass != null) {
            compass.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (compass != null) {
            compass.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (compass != null) {
            compass.stop();
        }
    }

    private void checkPermission() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getBearing();
        } else {
            locDescTv.setText(getResources().getString(R.string.msg_permission_not_granted_yet));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        /*setupCompass();*/

        /*compass = new Compass(getContext(), new CompassListener() {
            @Override
            public void onNewAzimuth(float azimuth) {
                adjustGambarDial(azimuth);
                adjustArrowQiblat(azimuth);
            }
        });*/
        /*Compass.CompassListener cl = new Compass.CompassListener() {

            @Override
            public void onNewAzimuth(float azimuth) {
                adjustGambarDial(azimuth);
                adjustArrowQiblat(azimuth);
            }
        };
        compass.setListener(cl);*/
    }

    private void setupCompass(){
        /*compass = new Compass(getContext(), new CompassListener() {
            @Override
            public void onNewAzimuth(float azimuth) {
                adjustGambarDial(azimuth);
                adjustArrowQiblat(azimuth);
            }
        });*/

        compass = new Compass(getContext(), new CompassListener() {
            @Override
            public void onNewAzimuth(float azimuth) {
                adjustGambarDial(azimuth);
                adjustArrowQiblat(azimuth);
            }

            @Override
            public void onAccelometerExist(boolean value) {
                if (value == false){
                    locDescTv.setText(getResources().getString(R.string.no_accelometer_sensor));
                }
            }

            @Override
            public void onMagneticFieldExist(boolean value) {
                if (value == false){
                    locDescTv.setText(getResources().getString(R.string.no_magnetic_field_sensor));
                }
            }
        });
    }

    public void adjustGambarDial(float azimuth) {
        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        imageDial.startAnimation(an);
    }

    public void adjustArrowQiblat(float azimuth) {
        float qibleDirection = GetFloat(QIBLE_DIRECTION);
        Animation an = new RotateAnimation(-(currentAzimuth) + qibleDirection, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        arrowViewQiblat.startAnimation(an);
        if (qibleDirection > 0) {
            arrowViewQiblat.setVisibility(View.VISIBLE);
        } else {
            arrowViewQiblat.setVisibility(INVISIBLE);
            arrowViewQiblat.setVisibility(View.GONE);
        }
    }

    @SuppressLint("MissingPermission")
    public void getBearing() {
        float qibleDirection = GetFloat(QIBLE_DIRECTION);
        if (qibleDirection > 0.0001) {
            locDescTv.setText(getResources().getString(R.string.qibla_direction) + " " + qibleDirection + " " + getResources().getString(R.string.degree_from_north));
            arrowViewQiblat.setVisibility(View.VISIBLE);
        } else {
            fetch_GPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetch_GPS();

                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.qible_location_access_required), Toast.LENGTH_LONG).show();
                }

                return;
            }
        }
    }

    public void SaveFloat(String Judul, Float qibleDirection) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putFloat(Judul, qibleDirection);
        edit.apply();
    }

    public Float GetFloat(String Judul) {
        Float qibleDirection = prefs.getFloat(Judul, 0);
        return qibleDirection;
    }

    public void fetch_GPS() {

        double result;
        gps = new GPSTracker(getContext());

        if (gps.canGetLocation()) {
            double lat_saya = gps.getLatitude();
            double lon_saya = gps.getLongitude();
            if (lat_saya < 0.001 && lon_saya < 0.001) {

                arrowViewQiblat.setVisibility(INVISIBLE);
                arrowViewQiblat.setVisibility(View.GONE);
                locDescTv.setText(getResources().getString(R.string.location_not_ready));

            } else {
                double longitude2 = QIBLE_LONGITUDE;
                double longitude1 = lon_saya;
                double latitude2 = Math.toRadians(QIBLE_LATITUDE);
                double latitude1 = Math.toRadians(lat_saya);
                double longDiff = Math.toRadians(longitude2 - longitude1);
                double y = Math.sin(longDiff) * Math.cos(latitude2);
                double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);
                result = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
                float result2 = (float) result;
                SaveFloat(QIBLE_DIRECTION, result2);
                locDescTv.setText(getResources().getString(R.string.qibla_direction) + " " + result2 + " " + getResources().getString(R.string.degree_from_north));
                arrowViewQiblat.setVisibility(View.VISIBLE);

            }
        } else {
            gps.showSettingsAlert();
            arrowViewQiblat.setVisibility(INVISIBLE);
            arrowViewQiblat.setVisibility(View.GONE);
            locDescTv.setText(getResources().getString(R.string.pls_enable_location));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gps != null)
            gps.stopUsingGPS();
    }
}
