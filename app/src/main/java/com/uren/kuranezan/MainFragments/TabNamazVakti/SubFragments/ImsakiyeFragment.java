package com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.uren.kuranezan.Interfaces.ListItemClickListener;
import com.uren.kuranezan.MainActivity;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.InternalStorage;
import com.uren.kuranezan.MainFragments.TabNamazVakti.Adapters.BaseListAdapter;
import com.uren.kuranezan.MainFragments.TabNamazVakti.Adapters.ImsakiyeAdapter;
import com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses.NamazHelper;
import com.uren.kuranezan.Models.LocationModels.Ilce;
import com.uren.kuranezan.Models.LocationModels.Sehir;
import com.uren.kuranezan.Models.LocationModels.Ulke;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.PrayerTimesList;
import com.uren.kuranezan.Utils.AdMobUtil.AdMobUtils;
import com.uren.kuranezan.Utils.Config;
import com.uren.kuranezan.Utils.DialogBoxUtil.DialogBoxUtil;
import com.uren.kuranezan.Utils.DialogBoxUtil.Interfaces.YesNoDialogBoxCallback;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_ILCE;
import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_NAMAZ_VAKITLERI;
import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_SEHIR;
import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_ULKE;
import static com.uren.kuranezan.Constants.StringConstants.CACHED_CITIES_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.CACHED_COUNTIES_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.CITY_LIST_URL_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.DISTINCT_LIST_URL_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.PRAYER_TIMES_INTERNAL_FILE_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.PRAYER_TIMES_LIST_URL_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_CITY;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_COUNTRY;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_COUNTY;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_COUNTY_CODE;


public class ImsakiyeFragment extends BaseFragment
        implements ListItemClickListener, View.OnClickListener {

    View mView;
    private LinearLayoutManager mLayoutManager;
    private ImsakiyeAdapter imsakiyeAdapter;
    private String toolbarTitle;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.imgLeft)
    ImageView imgBack;
    @BindView(R.id.adView)
    AdView adView;

    @BindArray(R.array.months)
    String[] aylar;

    AsyncTask<Void, Void, Void> asyncInsertData;

    public static ImsakiyeFragment newInstance(String toolbarTitle) {
        Bundle args = new Bundle();
        args.putString("toolbarTitle", toolbarTitle);
        ImsakiyeFragment fragment = new ImsakiyeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.GONE);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_imsakiye, container, false);
            ButterKnife.bind(this, mView);
            getItemsFromBundle();

            setToolbar();
            init();

            initRecyclerView();
            asyncInsertData = new AsyncInsertData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        return mView;
    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        AdMobUtils.loadInterstitialAd(getContext());
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            toolbarTitle = (String) args.get("toolbarTitle");
        }
    }

    private void initRecyclerView() {
        setLayoutManager();
        setAdapter();
    }

    private void setLayoutManager() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void setAdapter() {
        imsakiyeAdapter = new ImsakiyeAdapter(getContext());
        recyclerView.setAdapter(imsakiyeAdapter);
        imsakiyeAdapter.setListItemClickListener(this);
    }

    private void setToolbar() {
        txtToolbarTitle.setText(Config.county.toUpperCase() + " " + toolbarTitle);
    }


    @Override
    public void onListItemClick(Object object, int clickedPosition) {

    }


    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

    }


    /*****************************************************/
    private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("onInBackground()", "Data Inserting ");


            PrayerTimes[] prayerTimes = PrayerTimesList.getInstance().getPrayerTimes();
            final ArrayList<PrayerTimes> prayerTimesList = new ArrayList<PrayerTimes>();

            int currentDatePosition = 0;
            String formattedDate = getCurrentDate();

            for (int i = 0; i < prayerTimes.length; i++) {
                prayerTimesList.add(prayerTimes[i]);
                if (prayerTimes[i].getMiladiTarihKisa().equals(formattedDate)) {
                    currentDatePosition = i;
                }
            }

            final int finalCurrentDatePosition = currentDatePosition;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // WORK on UI thread here
                    setUpRecyclerView(prayerTimesList);
                    recyclerView.scrollToPosition(finalCurrentDatePosition);
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }


        private void setUpRecyclerView(ArrayList<PrayerTimes> prayerTimesList) {
            if (prayerTimesList != null && prayerTimesList.size() > 0) {
                imsakiyeAdapter.addAll(prayerTimesList);
            }
        }

        private String getCurrentDate() {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            return df.format(c);
        }


    }

    /*****************************************************/

    @Override
    public void onPause() {
        Log.i("onPause", "ok");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("onStop", "ok");
        super.onStop();
    }

    @Override
    public void onDestroy() {

        //Fragment tamamen kill edildigi zaman cagrilir.
        if (asyncInsertData != null)
            asyncInsertData.cancel(true);
        Log.i("onDestroy", "ok");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i("onDestroyView", "ok");
        super.onDestroyView();
    }
}
