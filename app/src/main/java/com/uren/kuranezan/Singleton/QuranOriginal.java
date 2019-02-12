package com.uren.kuranezan.Singleton;

import android.content.Context;
import android.os.AsyncTask;

import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.QuranAsyncProcess;
import com.uren.kuranezan.Models.QuranModels.Quran;

import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_ORIGINAL;

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
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onTaskContinue() {

            }
        }, REQUEST_TYPE_QURAN_ORIGINAL, "");

        quranAsyncProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);

    }

}
