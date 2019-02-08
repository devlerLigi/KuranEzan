package com.uren.kuranezan.MainFragments.TabKuran;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.uren.kuranezan.Interfaces.ListItemClickListener;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabKuran.Adapters.SurahAdapter;
import com.uren.kuranezan.MainFragments.TabKuran.SubFragments.SureDetayFragment;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.FileHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;


public class KuranFragment extends BaseFragment
        implements ListItemClickListener {

    View mView;
    private LinearLayoutManager mLayoutManager;
    private SurahAdapter surahAdapter;

    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<String> surahNameList = new ArrayList<String>();

    @BindArray(R.array.surah_name)
    String[] surahName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_kuran, container, false);
            ButterKnife.bind(this, mView);

            setToolbar();
            getJson();
            //setSurahList();

            initRecyclerView();
            setUpRecyclerView();
        }

        return mView;
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
        surahAdapter = new SurahAdapter(getContext());
        recyclerView.setAdapter(surahAdapter);
        surahAdapter.setListItemClickListener(this);
    }

    private void setUpRecyclerView() {

        for(int i=0; i<surahName.length; i++){
            surahNameList.add(surahName[i]);
        }

        if (surahNameList != null && surahNameList.size() > 0) {
            surahAdapter.addAll(surahNameList);
        }
    }

    private void getJson() {


                AsyncTask asyncTask = new AsyncTask() {

                    @Override
                    protected Object doInBackground(Object[] objects) {

                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://api.alquran.cloud/v1/surah/1")
                                .build();

                        Response response = null;


                        try {
                            response = client.newCall(request).execute();
                            return response.body().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {

                        String s= o.toString();
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        generateNoteOnSD(getContext(), "fatiha", s );

                    }


                }.execute();


    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {


        try {
            FileHelper.saveToFile( sBody);
            Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();

            String read =FileHelper.ReadFile(getContext());
                    Toast.makeText(getContext(), read, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

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
}
