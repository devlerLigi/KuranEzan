package com.uren.kuranezan.MainFragments.TabNamazVakti.SubFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.uren.kuranezan.MainFragments.TabNamazVakti.Adapters.BaseListAdapter;
import com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses.NamazHelper;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.InternalStorage;
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
import java.util.ArrayList;

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
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_COUNTRY;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_CITY;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_COUNTY;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_COUNTY_CODE;


public class BaseListFragment extends BaseFragment
        implements ListItemClickListener, View.OnClickListener {

    View mView;
    private LinearLayoutManager mLayoutManager;
    private BaseListAdapter baseListAdapter;
    private int itemType, requestedId;
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

    AsyncTask<Void, Void, Void> asyncInsertData;

    public static BaseListFragment newInstance(int itemType, int requestedId, String toolbarTitle) {
        Bundle args = new Bundle();
        args.putInt("itemType", itemType);
        args.putInt("requestedId", requestedId);
        args.putString("toolbarTitle", toolbarTitle);
        BaseListFragment fragment = new BaseListFragment();
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
            mView = inflater.inflate(R.layout.fragment_item_list, container, false);
            ButterKnife.bind(this, mView);
            getItemsFromBundle();

            setToolbar();

            init();
            initRecyclerView();
            setUpRecyclerView();

        }

        return mView;
    }

    private void init() {
        MobileAds.initialize(getContext(), getActivity().getResources().getString(R.string.ADMOB_APP_ID));
        AdMobUtils.loadBannerAd(adView);
        AdMobUtils.loadInterstitialAd(getContext());
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        recyclerView.setVisibility(View.GONE);
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            itemType = (Integer) args.getInt("itemType");
            requestedId = (Integer) args.getInt("requestedId");
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
        baseListAdapter = new BaseListAdapter(getContext());
        recyclerView.setAdapter(baseListAdapter);
        baseListAdapter.setListItemClickListener(this);
    }

    private void setUpRecyclerView() {
        asyncInsertData = new AsyncInsertData(itemType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setList(ArrayList<Object> objectList) {
        if (objectList != null && objectList.size() > 0) {
            baseListAdapter.addObject(objectList);
        }
    }

    private void setToolbar() {
        txtToolbarTitle.setText(toolbarTitle);
    }

    @Override
    public void onListItemClick(Object object, int clickedPosition) {

        if (object instanceof Ilce) {
            Ilce ilce = (Ilce) object;
            if (Config.transliterationlang.equals(Config.defaultTransliterationLang)) {
                SELECTED_COUNTY = ilce.getIlceAdi();
            } else {
                SELECTED_COUNTY = ilce.getIlceAdiEn();
            }
            SELECTED_COUNTY_CODE = ilce.getIlceID();
            setLocation(ilce);
        } else if (object instanceof Sehir) {
            Sehir sehir = (Sehir) object;
            if (Config.transliterationlang.equals(Config.defaultTransliterationLang)) {
                SELECTED_CITY = sehir.getSehirAdi();
            } else {
                SELECTED_CITY = sehir.getSehirAdiEn();
            }
            String title = getString(R.string.county);
            mFragmentNavigation.pushFragment(BaseListFragment.newInstance(ITEM_TYPE_ILCE, Integer.parseInt(sehir.getSehirID()), title));
        } else if (object instanceof Ulke) {
            Ulke ulke = (Ulke) object;
            if (Config.transliterationlang.equals(Config.defaultTransliterationLang)) {
                SELECTED_COUNTRY = ulke.getUlkeAdi();
            } else {
                SELECTED_COUNTRY = ulke.getUlkeAdiEn();
            }
            String title = getString(R.string.city);
            mFragmentNavigation.pushFragment(BaseListFragment.newInstance(ITEM_TYPE_SEHIR, Integer.parseInt(ulke.getUlkeID()), title));
        } else {

        }

    }

    private void setLocation(final Ilce ilce) {

        String message = getString(R.string.setLocation).replace("@ilce@", ilce.getIlceAdi());

        DialogBoxUtil.showYesNoDialog(getActivity(), "", message, new YesNoDialogBoxCallback() {
            @Override
            public void yesClick() {
                requestedId = Integer.parseInt(ilce.getIlceID());
                asyncInsertData = new AsyncInsertData(ITEM_TYPE_NAMAZ_VAKITLERI).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void noClick() {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

    }


    /*****************************************************/
    private class AsyncInsertData extends AsyncTask<Void, Void, Void> {

        private int requestType;

        public AsyncInsertData(int itemType) {
            this.requestType = itemType;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<Object> objectList = new ArrayList<Object>();

            if (requestType == ITEM_TYPE_ULKE) {
                Ulke[] ulkeList = getUlke();
                if (ulkeList != null) {
                    for (int i = 0; i < ulkeList.length; i++) {
                        objectList.add(ulkeList[i]);
                    }
                    setList(objectList);
                }
            } else if (requestType == ITEM_TYPE_SEHIR) {
                Sehir[] sehirList = getSehir();
                if (sehirList != null) {
                    for (int i = 0; i < sehirList.length; i++) {
                        objectList.add(sehirList[i]);
                    }
                    setList(objectList);
                }
            } else if (requestType == ITEM_TYPE_ILCE) {
                Ilce[] ilceList = getIlce();
                if (ilceList != null) {
                    for (int i = 0; i < ilceList.length; i++) {
                        objectList.add(ilceList[i]);
                    }
                    setList(objectList);
                }
            } else if (requestType == ITEM_TYPE_NAMAZ_VAKITLERI) {
                PrayerTimes[] prayerTimes = getPrayerTimes(requestedId);
                if (prayerTimes != null) {
                    PrayerTimesList.getInstance().setPrayerTimes(prayerTimes);
                    updateConfig();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NamazHelper.NamazVaktiRefresh.namazVaktiRefreshStart();
                            ((MainActivity) getActivity()).reSelectCurrentTab();
                        }
                    });


                }

            }

            return null;
        }

        private void updateConfig() {
            Config.country = SELECTED_COUNTRY;
            Config.city = SELECTED_CITY;
            Config.county = SELECTED_COUNTY;
            Config.countyCode = SELECTED_COUNTY_CODE;
            Config.update(getActivity());
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        private Ulke[] getUlke() {
            InputStream raw = getActivity().getResources().openRawResource(R.raw.ulkeler);
            Reader rd = new BufferedReader(new InputStreamReader(raw));

            Gson gson = new Gson();
            return gson.fromJson(rd, Ulke[].class);
        }

        private Sehir[] getSehir() {

            String endpointUrl = CITY_LIST_URL_PREFIX + requestedId;

            //CheckInternal
            Sehir[] cachedCities = readInternalCities(requestedId);
            if (cachedCities != null) {
                return cachedCities;
            }

            //NETWORK
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(endpointUrl)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                Gson gson = new Gson();
                Sehir[] cities = gson.fromJson(response.body().string(), Sehir[].class);
                saveInternalCities(requestedId, cities);
                return cities;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private Sehir[] readInternalCities(int index) {

            String key = CACHED_CITIES_PREFIX + index;
            Sehir[] cachedCities = null;

            // Retrieve the list from internal storage
            try {
                cachedCities = (Sehir[]) InternalStorage.readObject(getActivity(), key);
                Log.i("cache", "readInternalCities");
                return cachedCities;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void saveInternalCities(int index, Sehir[] cities) {
            // Save the list of entries to internal storage
            try {
                String key = CACHED_CITIES_PREFIX + index;
                InternalStorage.writeObject(getContext(), key, cities);
                Log.i("cache", "saveInternalCities");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Ilce[] getIlce() {

            String endpointUrl = DISTINCT_LIST_URL_PREFIX + requestedId;

            Ilce[] cachedCounties = readInternalCounties(requestedId);
            if (cachedCounties != null) {
                return cachedCounties;
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(endpointUrl)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                Gson gson = new Gson();
                Ilce[] counties = gson.fromJson(response.body().string(), Ilce[].class);
                saveInternalCounties(requestedId, counties);
                return counties;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private Ilce[] readInternalCounties(int index) {

            String key = CACHED_COUNTIES_PREFIX + index;
            Ilce[] cachedCounties = null;

            // Retrieve the list from internal storage
            try {
                cachedCounties = (Ilce[]) InternalStorage.readObject(getActivity(), key);
                Log.i("cache", "readInternalCounties");
                return cachedCounties;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void saveInternalCounties(int index, Ilce[] counties) {
            // Save the list of entries to internal storage
            try {
                String key = CACHED_COUNTIES_PREFIX + index;
                InternalStorage.writeObject(getContext(), key, counties);
                Log.i("cache", "saveInternalCounties");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private PrayerTimes[] getPrayerTimes(int ilceId) {

            String endpointUrl = PRAYER_TIMES_LIST_URL_PREFIX + ilceId;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(endpointUrl)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                String content = response.body().string();
                Gson gson = new Gson();
                PrayerTimes[] prayerTimes = gson.fromJson(content, PrayerTimes[].class);
                saveFileToInternal(getActivity(), content);
                return prayerTimes;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private void saveFileToInternal(Context context, String file) {

            String fileContents = file;
            FileOutputStream fos = null;
            String fileName = getInternalFileName();

            try {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(fileContents.getBytes());
                String path = context.getFilesDir() + "/" + fileName;
                Log.i("saved to", path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private String getInternalFileName() {
            return PRAYER_TIMES_INTERNAL_FILE_PREFIX + getFileIdentifier() + ".json";
        }

        private String getFileIdentifier() {
            return SELECTED_COUNTY_CODE;
        }

    }

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
