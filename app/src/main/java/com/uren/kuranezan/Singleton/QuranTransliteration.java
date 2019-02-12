package com.uren.kuranezan.Singleton;

import android.content.Context;
import android.os.AsyncTask;

import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.QuranAsyncProcess;
import com.uren.kuranezan.Models.QuranModels.Quran;

import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_TRANSLITERATION;

public class QuranTransliteration {

    private static QuranTransliteration single_instance = null;
    private Quran quranTransliteration;

    private QuranTransliteration(Context context, String lang) {
        parseJson(context, lang);
    }

    public static QuranTransliteration getInstance(Context context, String lang) {
        if (single_instance == null)
            single_instance = new QuranTransliteration(context, lang);


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

    private void parseJson(final Context context, String lang) {

        QuranAsyncProcess quranAsyncProcess = new QuranAsyncProcess(new OnEventListener<Quran>() {
            @Override
            public void onSuccess(Quran quran) {
                setQuranTransliteration(quran);
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onTaskContinue() {

            }
        }, REQUEST_TYPE_QURAN_TRANSLITERATION, lang);

        quranAsyncProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);

    }

}
