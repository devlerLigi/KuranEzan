package com.uren.kuranezan.MainFragments.TabKuran.SubFragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.uren.kuranezan.Interfaces.CompleteCallback;
import com.uren.kuranezan.Interfaces.OptionsCallback;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabKuran.Adapters.AyahAdapter;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.OptionsHelper;
import com.uren.kuranezan.Models.QuranModels.Ayahs;
import com.uren.kuranezan.Models.QuranModels.Quran;
import com.uren.kuranezan.Models.QuranModels.Surahs;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Singleton.QuranOriginal;
import com.uren.kuranezan.Singleton.QuranTranslation;
import com.uren.kuranezan.Singleton.QuranTransliteration;
import com.uren.kuranezan.Utils.ClickableImage.ClickableImageView;
import com.uren.kuranezan.Utils.Config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_TRANSLATION_DEFAULT;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_TRANSLATION_OTHER;
import static com.uren.kuranezan.Constants.StringConstants.INTERNAL_FILE_NAME_PREFIX;


public class SureDetayFragment extends BaseFragment
        implements View.OnClickListener {

    View mView;
    private int number;
    private LinearLayoutManager mLayoutManager;
    private AyahAdapter ayahAdapter;

    @BindView(R.id.toolbarLayout)
    Toolbar toolbarLayout;
    @BindView(R.id.txtToolbarTitle)
    TextView txtToolbarTitle;
    @BindView(R.id.imgLeft)
    ImageView imgBack;
    @BindView(R.id.imgRight)
    ImageView imgOptions;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    ArrayList<Ayahs> ayahOriginalList = new ArrayList<Ayahs>();
    ArrayList<Ayahs> ayahTransliterationlList = new ArrayList<Ayahs>();
    ArrayList<Ayahs> ayahTranslationList = new ArrayList<Ayahs>();

    List<AsyncTask<Void, Void, Void>> asyncTasks = new ArrayList<AsyncTask<Void, Void, Void>>();

    public static SureDetayFragment newInstance(int number) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, number);
        SureDetayFragment fragment = new SureDetayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SureDetayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_suredetay, container, false);
            ButterKnife.bind(this, mView);

            checkBundle();
            setToolbar();
            init();

            initRecyclerView();
            setQuranModels();

            /*
            AsyncTask<Void, Void, Void> a1 = new AsyncInsertData1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            AsyncTask<Void, Void, Void> a2 = new AsyncInsertData2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            AsyncTask<Void, Void, Void> a3 = new AsyncInsertData3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            asyncTasks.add(a1);
            asyncTasks.add(a2);
            asyncTasks.add(a3);
            */

        }

        return mView;
    }

    private void setQuranModels() {

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        setOriginal();
        setTransliteration();
        setTranslation();
    }


    private void setOriginal() {

        QuranOriginal instance = QuranOriginal.getInstance();

        if (instance == null) {
            Log.i("original-1", "instance oluşturuldu");
            QuranOriginal.getInstance(getActivity(), new CompleteCallback<Quran>() {

                @Override
                public void onComplete(Quran quranOriginal) {
                    Log.i("original-2", "listener1 den set edildi");
                    setAyahOriginal(quranOriginal);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.i("original-fail", e.toString());
                }
            });
        } else {

            if (instance.getQuranOriginal() != null) {
                Log.i("original-3", "mevcuttan alındı");
                setAyahOriginal(instance.getQuranOriginal());
            } else {
                QuranOriginal.setCompleteCallback(new CompleteCallback<Quran>() {
                    @Override
                    public void onComplete(Quran quranOriginal) {
                        Log.i("original-2", "listener2 den set edildi");
                        setAyahOriginal(quranOriginal);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.i("original-fail", e.toString());
                    }
                });
            }
        }

    }

    private void setAyahOriginal(Quran quranOriginal) {
        Surahs[] surahs = quranOriginal.getData().getSurahs();
        Ayahs[] ayahs = surahs[number].getAyahs();

        for (int i = 0; i < ayahs.length; i++) {
            ayahOriginalList.add(ayahs[i]);
        }

        checkAllTasks();
    }

    private void setTransliteration() {

        QuranTransliteration instance = QuranTransliteration.getInstance();

        if (instance == null) {
            Log.i("transliteration-1", "instance oluşturuldu");
            QuranTransliteration.getInstance(getActivity(), Config.transliterationlang, new CompleteCallback<Quran>() {
                @Override
                public void onComplete(Quran quranTransliteration) {
                    Log.i("transliteration-2", "listener1 den set edildi");
                    setAyahTransliteration(quranTransliteration);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.i("transliteration-fail", e.toString());
                }
            });
        } else {
            if (instance.getQuranTransliteration() != null) {
                Log.i("transliteration-3", "mevcuttan alındı");
                setAyahTransliteration(instance.getQuranTransliteration());
            } else {
                QuranTransliteration.setCompleteCallback(new CompleteCallback<Quran>() {
                    @Override
                    public void onComplete(Quran quranTransliteration) {
                        Log.i("transliteration-2", "listener2 den set edildi");
                        setAyahTransliteration(quranTransliteration);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.i("transliteration-fail", e.toString());
                    }
                });
            }
        }

    }

    private void setAyahTransliteration(Quran quranTransliteration) {
        Surahs[] surahs = quranTransliteration.getData().getSurahs();
        Ayahs[] ayahs = surahs[number].getAyahs();

        for (int i = 0; i < ayahs.length; i++) {
            ayahTransliterationlList.add(ayahs[i]);
        }

        checkAllTasks();
    }

    private void setTranslation() {

        QuranTranslation instance = QuranTranslation.getInstance();

        if (instance == null) {
            Log.i("translation-1", "instance oluşturuldu");
            QuranTranslation.getInstance(getActivity(), Config.lang, new CompleteCallback<Quran>() {
                @Override
                public void onComplete(Quran quranTranslation) {
                    Log.i("translation-2", "listener1 den set edildi");
                    setAyahTranslation(quranTranslation);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.i("translation-fail", e.toString());
                }
            });
        } else {
            if (instance.getQuranTranslation() != null) {
                Log.i("translation-3", "mevcuttan alındı");
                setAyahTranslation(instance.getQuranTranslation());
            } else {
                QuranTranslation.setCompleteCallback(new CompleteCallback<Quran>() {
                    @Override
                    public void onComplete(Quran quranTranslation) {
                        Log.i("translation-2", "listener2 den set edildi");
                        setAyahTranslation(quranTranslation);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.i("translation-fail", e.toString());
                    }
                });
            }


        }
    }

    private void setAyahTranslation(Quran quranTranslation) {
        Surahs[] surahs = quranTranslation.getData().getSurahs();
        Ayahs[] ayahs = surahs[number].getAyahs();

        for (int i = 0; i < ayahs.length; i++) {
            ayahTranslationList.add(ayahs[i]);
        }

        checkAllTasks();
    }

    private void checkAllTasks() {
        if (QuranOriginal.isFinished() && QuranTransliteration.isFinished() && QuranTranslation.isFinished()) {
            setUpRecyclerView();
        }
    }

    private void checkBundle() {
        Bundle args = getArguments();
        if (args != null) {
            number = (Integer) args.getInt(ARGS_INSTANCE);
        } else {
            getActivity().onBackPressed();
        }
    }

    private void setToolbar() {
        String surahName = getResources().getStringArray(R.array.surah_name)[number];
        txtToolbarTitle.setText(surahName);
    }

    private void init() {
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);
        imgOptions.setVisibility(View.VISIBLE);
        imgOptions.setOnClickListener(this);
        recyclerView.setOnClickListener(this);
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
        ayahAdapter = new AyahAdapter(getContext());
        recyclerView.setAdapter(ayahAdapter);
        //ayahAdapter.setListItemClickListener(this);
    }

    private void setUpRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        ayahAdapter.addAll(ayahOriginalList, ayahTransliterationlList, ayahTranslationList);
    }

    @Override
    public void onClick(View view) {

        if (view == imgBack) {
            getActivity().onBackPressed();
        }

        if (view == imgOptions) {
            imgOptionsClicked();
        }

    }

    private void imgOptionsClicked() {
        OptionsHelper.OptionsClicked optionsClicked = OptionsHelper.OptionsClicked.getInstance();
        optionsClicked.setSinglePostItems(mFragmentNavigation);
        optionsClicked.setProfileRefreshCallback(new OptionsCallback() {
            @Override
            public void onLanguageChanged(String language) {
                ayahTranslationList.clear();
                Quran quranTranslation = QuranTranslation.getInstance().getQuranTranslation();
                Surahs[] surahs = quranTranslation.getData().getSurahs();
                Ayahs[] ayahs = surahs[number].getAyahs();

                for (int i = 0; i < ayahs.length; i++) {
                    ayahTranslationList.add(ayahs[i]);
                }

                ayahAdapter.updateLanguage(ayahTranslationList);
            }

            @Override
            public void onTransliterationLanguageChanged(String language) {
                ayahTransliterationlList.clear();
                Quran quranTransliteration = QuranTransliteration.getInstance().getQuranTransliteration();
                Surahs[] surahs = quranTransliteration.getData().getSurahs();
                Ayahs[] ayahs = surahs[number].getAyahs();

                for (int i = 0; i < ayahs.length; i++) {
                    ayahTransliterationlList.add(ayahs[i]);
                }

                ayahAdapter.updateTransliterationLanguage(ayahTransliterationlList);
            }

            @Override
            public void onShowTransliterationChanged(boolean isShow) {
                ayahAdapter.showTransliteration(isShow);
            }

            @Override
            public void onShowTranslationChanged(boolean isShow) {
                ayahAdapter.showTranslation(isShow);
            }

            @Override
            public void onFontArabicChanged(String fontType) {
                ayahAdapter.updateFontArabic(fontType);
            }

            @Override
            public void onFontSizeArabicChanged(int fontSize) {
                ayahAdapter.updateFontSizeArabic(fontSize);
            }

            @Override
            public void onFontSizeTransliterationChanged(int fontSize) {
                ayahAdapter.updateFontSizeTransliteration(fontSize);
            }

            @Override
            public void onFontSizeTranslationChanged(int fontSize) {
                ayahAdapter.updateFontSizeTranslation(fontSize);
            }
        });

        optionsClicked.startOptionsProcess();

    }

    /****************************************************************************************************/


    /*private class AsyncInsertData1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("info1", "async1 started");
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            final ObjectMapper mapper = new ObjectMapper();

            setAyahListOriginal(mapper);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            List<String> statusList = new ArrayList<String>();

            boolean allFinished = true;
            for (int i = 0; i < asyncTasks.size(); i++) {
                AsyncTask<Void, Void, Void> asyncTaskItem = (AsyncTask<Void, Void, Void>) asyncTasks.get(i);
                // getStatus() would return PENDING,RUNNING,FINISHED statuses
                if (asyncTaskItem instanceof AsyncInsertData1) {

                } else {
                    if (asyncTaskItem.getStatus() != (Status.FINISHED)) {
                        allFinished = false;
                    }
                }

            }

            if (allFinished) {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                ayahAdapter.addAll(ayahOriginalList, ayahTransliterationlList, ayahTranslationList);
            }

            Log.i("info1", "async1 finished");

        }


        private void setAyahListOriginal(ObjectMapper mapper) {

            Quran quranOriginal = QuranOriginal.getInstance(getContext()).getQuranOriginal();
            if (quranOriginal == null) {

                long lStartTime = System.currentTimeMillis();
*//*
                InputStream raw = getContext().getResources().openRawResource(R.raw.quran_uthmani);
                Reader rd = new BufferedReader(new InputStreamReader(raw));
                // Now do the magic.
                QuranOriginal.getInstance().setQuranOriginal(new Gson().fromJson(rd, Quran.class));
*//*

                try {

                    InputStream raw = getContext().getResources().openRawResource(R.raw.quran_uthmani);
                    Reader rd = new BufferedReader(new InputStreamReader(raw));

                    Quran q = mapper.readValue(rd, Quran.class);
                    QuranOriginal.getInstance().setQuranOriginal(q);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("parse_error ", e.toString());
                }


                long lEndTime = System.currentTimeMillis();
                long output = lEndTime - lStartTime;

                Log.i("original ", String.valueOf(output));

            }
            quranOriginal = QuranOriginal.getInstance().getQuranOriginal();


            Surahs[] surahs = quranOriginal.getData().getSurahs();
            Ayahs[] ayahs = surahs[number].getAyahs();

            for (int i = 0; i < ayahs.length; i++) {
                ayahOriginalList.add(ayahs[i]);
            }
        }

    }*/

    /*private class AsyncInsertData2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("info2", "async2 started");
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            final ObjectMapper mapper = new ObjectMapper();

            setAyahTransliteration(mapper);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            List<String> statusList = new ArrayList<String>();

            boolean allFinished = true;
            for (int i = 0; i < asyncTasks.size(); i++) {
                AsyncTask<Void, Void, Void> asyncTaskItem = (AsyncTask<Void, Void, Void>) asyncTasks.get(i);
                // getStatus() would return PENDING,RUNNING,FINISHED statuses
                if (asyncTaskItem instanceof AsyncInsertData2) {

                } else {
                    if (asyncTaskItem.getStatus() != (Status.FINISHED)) {
                        allFinished = false;
                    }
                }


            }

            if (allFinished) {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                ayahAdapter.addAll(ayahOriginalList, ayahTransliterationlList, ayahTranslationList);
            }

            Log.i("info2", "async2 finished");

        }


        private void setAyahTransliteration(ObjectMapper mapper) {

            Quran quranTransliteration = QuranTransliteration.getInstance(getContext(), Config.transliterationlang).getQuranTransliteration();
            if (quranTransliteration == null) {
                String identifier = Config.transliterationlang;

                int rawNum;
                if (identifier.equals(Config.defaultTransliterationLang)) {
                    rawNum = R.raw.quran_transliteration_tr;
                } else {
                    rawNum = R.raw.quran_transliteration_en;
                }

                long lStartTime = System.currentTimeMillis();
                try {

                    InputStream raw = getContext().getResources().openRawResource(rawNum);
                    Reader rd = new BufferedReader(new InputStreamReader(raw));

                    Quran q = mapper.readValue(rd, Quran.class);
                    QuranTransliteration.getInstance().setQuranTransliteration(q);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("parse_error ", e.toString());
                }


                long lEndTime = System.currentTimeMillis();
                long output = lEndTime - lStartTime;

                Log.i("transliteration ", String.valueOf(output));

            }
            quranTransliteration = QuranTransliteration.getInstance().getQuranTransliteration();


            Surahs[] surahs = quranTransliteration.getData().getSurahs();
            Ayahs[] ayahs = surahs[number].getAyahs();

            for (int i = 0; i < ayahs.length; i++) {
                ayahTransliterationlList.add(ayahs[i]);
            }
        }

    }*/

    /*private class AsyncInsertData3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i("info3", "async3 started");
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            final ObjectMapper mapper = new ObjectMapper();

            setAyahTranslation(mapper);

            *//*
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // WORK on UI thread here
                    setUpRecyclerView();

                }
            });
*//*
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            List<String> statusList = new ArrayList<String>();

            boolean allFinished = true;
            for (int i = 0; i < asyncTasks.size(); i++) {
                AsyncTask<Void, Void, Void> asyncTaskItem = (AsyncTask<Void, Void, Void>) asyncTasks.get(i);
                // getStatus() would return PENDING,RUNNING,FINISHED statuses
                if (asyncTaskItem instanceof AsyncInsertData3) {

                } else {
                    if (asyncTaskItem.getStatus() != (Status.FINISHED)) {
                        allFinished = false;
                    }
                }


            }

            if (allFinished) {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                ayahAdapter.addAll(ayahOriginalList, ayahTransliterationlList, ayahTranslationList);
            }

            Log.i("info3", "async3 finished");
        }

        private void setAyahTranslation(ObjectMapper mapper) {

            Quran quranTranslation = QuranTranslation.getInstance(getContext(), Config.lang).getQuranTranslation();
            if (quranTranslation == null) {
                String identifier = Config.lang;
                int requestType;
                if (identifier.equals(Config.defaultLang)) {
                    requestType = REQUEST_TYPE_QURAN_TRANSLATION_DEFAULT;
                } else {
                    requestType = REQUEST_TYPE_QURAN_TRANSLATION_OTHER;
                }

                Quran q;
                if (requestType == REQUEST_TYPE_QURAN_TRANSLATION_DEFAULT) {
                    long lStartTime = System.currentTimeMillis();
                    q = setQuranTranslation(getContext(), mapper);

                    long lEndTime = System.currentTimeMillis();
                    long output = lEndTime - lStartTime;
                    Log.i("translation default ", String.valueOf(output));
                } else if (requestType == REQUEST_TYPE_QURAN_TRANSLATION_OTHER) {
                    long lStartTime = System.currentTimeMillis();
                    q = selectedQuranTranslation(getContext(), identifier, mapper);
                    long lEndTime = System.currentTimeMillis();
                    long output = lEndTime - lStartTime;
                    Log.i("translation selected", String.valueOf(output));
                } else {
                    return;
                }

                QuranTranslation.getInstance().setQuranTranslation(q);

            }
            quranTranslation = QuranTranslation.getInstance().getQuranTranslation();

            Surahs[] surahs = quranTranslation.getData().getSurahs();
            Ayahs[] ayahs = surahs[number].getAyahs();

            for (int i = 0; i < ayahs.length; i++) {
                ayahTranslationList.add(ayahs[i]);
            }
        }

        private Quran setQuranTranslation(Context context, ObjectMapper mapper) {

            try {

                InputStream raw = context.getResources().openRawResource(R.raw.quran_translation_tr_diyanet);
                Reader rd = new BufferedReader(new InputStreamReader(raw));

                return mapper.readValue(rd, Quran.class);


            } catch (IOException e) {
                e.printStackTrace();
                Log.i("parse_error ", e.toString());
            }

            return null;
        }

        private Quran selectedQuranTranslation(Context context, String identifier, ObjectMapper mapper) {

            String fileName = getInternalFileName(identifier);
            FileInputStream fis = null;
            try {


                try {
                    fis = context.openFileInput(fileName);
                    Reader rd = new BufferedReader(new InputStreamReader(fis));

                    return mapper.readValue(rd, Quran.class);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("parse_error ", e.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;

        }

        private String getInternalFileName(String identifier) {
            return INTERNAL_FILE_NAME_PREFIX + getFileIdentifier(identifier) + ".json";
        }

        private String getFileIdentifier(String identifier) {
            return identifier.replace('.', '_');
        }


    }*/

}
