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

public class QuranTranslation {

    private static QuranTranslation single_instance = null;
    private Quran quranTranslation;

    private QuranTranslation(Context context) {
        parseJson(context);
    }

    public static QuranTranslation getInstance(Context context) {
        if (single_instance == null)
            single_instance = new QuranTranslation(context);


        return single_instance;
    }

    public static QuranTranslation getInstance() {
        return single_instance;
    }

    public Quran getQuranTranslation() {
        return quranTranslation;
    }

    public void setQuranTranslation(Quran quranOriginal) {
        this.quranTranslation = quranOriginal;
    }

    private void parseJson(final Context context) {

        QuranAsyncProcess quranAsyncProcess = new QuranAsyncProcess(new OnEventListener<Quran>() {
            @Override
            public void onSuccess(Quran quran) {
                setQuranTranslation(quran);
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onTaskContinue() {

            }
        }, 3);

        quranAsyncProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);

    }

}
