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

import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.KibleBul.Compass;
import com.uren.kuranezan.MainFragments.TabDiger.SubFragments.KibleBul.GPSTracker;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.INVISIBLE;


public class KibleBulFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;

    private Compass compass;

    @BindView(R.id.main_image_qiblat)
    ImageView arrowViewQiblat;
    @BindView(R.id.main_image_dial)
    ImageView imageDial;
    @BindView(R.id.teks_atas)
    TextView text_atas;
    @BindView(R.id.teks_bawah)
    TextView text_bawah;

    public Menu menu;
    public MenuItem item;
    private float currentAzimuth;
    SharedPreferences prefs;
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_kible_bul, container, false);
            ButterKnife.bind(this, mView);
            init();
        }

        return mView;
    }

    private void init() {
        prefs = getContext().getSharedPreferences("", MODE_PRIVATE);
        gps = new GPSTracker(getContext());
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        arrowViewQiblat.setVisibility(INVISIBLE);
        arrowViewQiblat.setVisibility(View.GONE);
        setupCompass();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.VISIBLE);
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

    private void setupCompass() {
        Boolean permission_granted = GetBoolean("permission_granted");
        if (permission_granted) {
            getBearing();
        } else {
            text_atas.setText(getResources().getString(R.string.msg_permission_not_granted_yet));
            text_bawah.setText(getResources().getString(R.string.msg_permission_not_granted_yet));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                /*requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);*/
            }
        }

        compass = new Compass(getContext());
        Compass.CompassListener cl = new Compass.CompassListener() {

            @Override
            public void onNewAzimuth(float azimuth) {
                // adjustArrow(azimuth);
                adjustGambarDial(azimuth);
                adjustArrowQiblat(azimuth);
            }
        };
        compass.setListener(cl);
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
        float kiblat_derajat = GetFloat("kiblat_derajat");
        Animation an = new RotateAnimation(-(currentAzimuth) + kiblat_derajat, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        arrowViewQiblat.startAnimation(an);
        if (kiblat_derajat > 0) {
            arrowViewQiblat.setVisibility(View.VISIBLE);
        } else {
            arrowViewQiblat.setVisibility(INVISIBLE);
            arrowViewQiblat.setVisibility(View.GONE);
        }
    }

    @SuppressLint("MissingPermission")
    public void getBearing() {
        float kiblat_derajat = GetFloat("kiblat_derajat");
        if (kiblat_derajat > 0.0001) {
            text_bawah.setText(getResources().getString(R.string.your_location) + " " + getResources().getString(R.string.using_last_location));
            text_atas.setText(getResources().getString(R.string.qibla_direction) + " " + kiblat_derajat + " " + getResources().getString(R.string.degree_from_north));
            // MenuItem item = menu.findItem(R.id.gps);
            if (item != null) {
                item.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.gps_off));
            }
            arrowViewQiblat.setVisibility(View.VISIBLE);
        } else {
            fetch_GPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    SaveBoolean("permission_granted", true);
                    text_atas.setText(getResources().getString(R.string.msg_permission_granted));
                    text_bawah.setText(getResources().getString(R.string.msg_permission_granted));
                    arrowViewQiblat.setVisibility(INVISIBLE);
                    arrowViewQiblat.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_permission_required), Toast.LENGTH_LONG).show();
                    //getActivity().onBackPressed();
                }
                return;
            }
        }
    }

    public void SaveString(String Judul, String tex) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(Judul, tex);
        edit.apply();
    }

    public String GetString(String Judul) {
        String Stringxxx = prefs.getString(Judul, "");
        return Stringxxx;
    }

    public void SaveBoolean(String Judul, Boolean bbb) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(Judul, bbb);
        edit.apply();
    }

    public Boolean GetBoolean(String Judul) {
        Boolean result = prefs.getBoolean(Judul, false);
        return result;
    }

    public void Savelong(String Judul, Long bbb) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(Judul, bbb);
        edit.apply();
    }

    public Long Getlong(String Judul) {
        Long xxxxxx = prefs.getLong(Judul, 0);
        return xxxxxx;
    }

    public void SaveFloat(String Judul, Float bbb) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putFloat(Judul, bbb);
        edit.apply();
    }

    public Float GetFloat(String Judul) {
        Float xxxxxx = prefs.getFloat(Judul, 0);
        return xxxxxx;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gps, menu);
        item = menu.findItem(R.id.gps);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.gps:
                //logout code
                fetch_GPS();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fetch_GPS() {

        double result = 0;
        gps = new GPSTracker(getContext());
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            text_bawah.setText(getResources().getString(R.string.your_location) + "\nLat: " + latitude + " Long: " + longitude);

            double lat_saya = gps.getLatitude();
            double lon_saya = gps.getLongitude();
            if (lat_saya < 0.001 && lon_saya < 0.001) {

                arrowViewQiblat.setVisibility(INVISIBLE);
                arrowViewQiblat.setVisibility(View.GONE);
                text_atas.setText(getResources().getString(R.string.location_not_ready));
                text_bawah.setText(getResources().getString(R.string.location_not_ready));

                if (item != null)
                    item.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.gps_off));

            } else {
                if (item != null)
                    item.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.gps_on));

                double longitude2 = 39.826206; // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                double longitude1 = lon_saya;
                double latitude2 = Math.toRadians(21.422487); // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                double latitude1 = Math.toRadians(lat_saya);
                double longDiff = Math.toRadians(longitude2 - longitude1);
                double y = Math.sin(longDiff) * Math.cos(latitude2);
                double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);
                result = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
                float result2 = (float) result;
                SaveFloat("kiblat_derajat", result2);
                text_atas.setText(getResources().getString(R.string.qibla_direction) + " " + result2 + " " + getResources().getString(R.string.degree_from_north));
                Toast.makeText(getContext(), getResources().getString(R.string.qibla_direction) + " " + result2 + " " + getResources().getString(R.string.degree_from_north), Toast.LENGTH_LONG).show();
                arrowViewQiblat.setVisibility(View.VISIBLE);

            }
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();

            // arrowViewQiblat.isShown(false);
            arrowViewQiblat.setVisibility(INVISIBLE);
            arrowViewQiblat.setVisibility(View.GONE);
            text_atas.setText(getResources().getString(R.string.pls_enable_location));
            text_bawah.setText(getResources().getString(R.string.pls_enable_location));

            if (item != null)
                item.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.gps_off));
        }
    }

    @Override
    public void onClick(View view) {


    }
}
