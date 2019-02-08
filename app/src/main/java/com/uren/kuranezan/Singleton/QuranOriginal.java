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

public class QuranOriginal {

    private static QuranOriginal single_instance = null;
    private Quran quranOriginal;

    private QuranOriginal(Context context) {
        parseJson(context);
    }

    public static QuranOriginal getInstance(Context context) {
        if (single_instance == null)
            single_instance = new QuranOriginal(context);


        return single_instance;
    }

    public static QuranOriginal getInstance() {
        return single_instance;
    }

    public Quran getQuranOriginal() {
        return quranOriginal;
    }

    public void setQuranOriginal(Quran quranOriginal) {
        this.quranOriginal = quranOriginal;
    }

    private void parseJson(final Context context) {

        QuranAsyncProcess quranAsyncProcess = new QuranAsyncProcess(new OnEventListener<Quran>() {
            @Override
            public void onSuccess(Quran quran) {
                setQuranOriginal(quran);
                Toast.makeText(context, "setQuranOriginal-ok", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onTaskContinue() {

            }
        }, 1);

        quranAsyncProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);

    }

}
