package com.uren.kuranezan.MainFragments.TabImsakiye.SubFragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.uren.kuranezan.Interfaces.ListItemClickListener;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabImsakiye.Adapters.BaseListAdapter;
import com.uren.kuranezan.MainFragments.TabKuran.SubFragments.SureDetayFragment;
import com.uren.kuranezan.Models.LocationModels.Ilce;
import com.uren.kuranezan.Models.LocationModels.Sehir;
import com.uren.kuranezan.Models.LocationModels.Ulke;
import com.uren.kuranezan.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_ILCE;
import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_SEHIR;
import static com.uren.kuranezan.Constants.NumericConstants.ITEM_TYPE_ULKE;
import static com.uren.kuranezan.Constants.StringConstants.CITY_LIST_URL_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.DISTINCT_LIST_URL_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.QURAN_TRANSLATION_URL_PREFIX;


public class BaseListFragment extends BaseFragment
        implements ListItemClickListener {

    View mView;
    private LinearLayoutManager mLayoutManager;
    private BaseListAdapter baseListAdapter;
    private int itemType, requestedId;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    ArrayList<Object> objectList = new ArrayList<Object>();


    public static BaseListFragment newInstance(int itemType, int requestedId) {
        Bundle args = new Bundle();
        args.putInt("itemType", itemType);
        args.putInt("requestedId", requestedId);
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
        getActivity().findViewById(R.id.tabMainLayout).setVisibility(View.VISIBLE);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_item_list, container, false);
            ButterKnife.bind(this, mView);
            recyclerView.setVisibility(View.GONE);
            getItemsFromBundle();
            setToolbar();

            initRecyclerView();
            setUpRecyclerView();

        }

        return mView;
    }

    private void getItemsFromBundle() {
        Bundle args = getArguments();
        if (args != null) {
            itemType = (Integer) args.getInt("itemType");
            requestedId = (Integer) args.getInt("requestedId");
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

        progressBar.setVisibility(View.VISIBLE);

        if (itemType == ITEM_TYPE_ULKE) {
            Ulke[] ulkeList = getUlke();
            for (int i = 0; i < ulkeList.length; i++) {
                objectList.add(ulkeList[i]);
            }
        } else if (itemType == ITEM_TYPE_SEHIR) {
            Sehir[] sehirList = getSehir();
            for (int i = 0; i < sehirList.length; i++) {
                objectList.add(sehirList[i]);
            }
        } else if (itemType == ITEM_TYPE_ILCE) {
            Ilce[] ilceList = getIlce();
            for (int i = 0; i < ilceList.length; i++) {
                objectList.add(ilceList[i]);
            }
        } else {
            return;
        }

        if (objectList != null && objectList.size() > 0) {
            baseListAdapter.addObject(objectList);
        }

        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }


    private Ulke[] getUlke() {
        InputStream raw = getContext().getResources().openRawResource(R.raw.ulkeler);
        Reader rd = new BufferedReader(new InputStreamReader(raw));

        Gson gson = new Gson();
        return gson.fromJson(rd, Ulke[].class);
    }

    private Sehir[] getSehir() {

        String endpointUrl = CITY_LIST_URL_PREFIX + requestedId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointUrl)
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
            Gson gson = new Gson();
            return gson.fromJson(response.body().string(), Sehir[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Ilce[] getIlce() {

        String endpointUrl = DISTINCT_LIST_URL_PREFIX + requestedId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointUrl)
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
            Gson gson = new Gson();
            return gson.fromJson(response.body().string(), Ilce[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setToolbar() {
        txtToolbarTitle.setText(getString(R.string.quran));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onListItemClick(String surahName, int clickedPosition) {
        mFragmentNavigation.pushFragment(SureDetayFragment.newInstance(clickedPosition));
    }


    /*****************************************************/


}
