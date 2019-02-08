package com.uren.kuranezan.Singleton;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.QuranAsyncProcess;
import com.uren.kuranezan.Models.Quran;
import com.uren.kuranezan.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class QuranTransliteration {

    private static QuranTransliteration single_instance = null;
    private Quran quranTransliteration;

    private QuranTransliteration(Context context) {
        parseJson(context);
    }

    public static QuranTransliteration getInstance(Context context) {
        if (single_instance == null)
            single_instance = new QuranTransliteration(context);


        return single_instance;
    }

    public static QuranTransliteration getInstance() {
        return single_instance;
    }

    public Quran getQuranTransliteration() {
        return quranTransliteration;
    }

    public void setQuranTransliteration(Quran quranTransliteration) {
        this.quranTransliteration = quranTransliteration;
    }

    private void parseJson(final Context context) {

        QuranAsyncProcess quranAsyncProcess = new QuranAsyncProcess(new OnEventListener<Quran>() {
            @Override
            public void onSuccess(Quran quran) {
                setQuranTransliteration(quran);
                Toast.makeText(context, "setQuranTransliteration-ok", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onTaskContinue() {

            }
        }, 2);

        quranAsyncProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);

    }

}
