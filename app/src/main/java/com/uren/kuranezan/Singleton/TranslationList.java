package com.uren.kuranezan.Singleton;

import android.content.Context;
import android.os.AsyncTask;

import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.TranslationsAsyncProcess;
import com.uren.kuranezan.Models.TranslationModels.Translations;

public class TranslationList {

    private static TranslationList single_instance = null;
    private Translations translations;

    private TranslationList(Context context) {
        parseJson(context);
    }

    public static TranslationList getInstance(Context context) {
        if (single_instance == null)
            single_instance = new TranslationList(context);


        return single_instance;
    }

    public static TranslationList getInstance() {
        return single_instance;
    }

    public Translations getTranslations() {
        return translations;
    }

    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    private void parseJson(final Context context) {

        TranslationsAsyncProcess translationsAsyncProcess = new TranslationsAsyncProcess(new OnEventListener<Translations>() {
            @Override
            public void onSuccess(Translations translations) {
                setTranslations(translations);
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onTaskContinue() {

            }
        });

        translationsAsyncProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);

    }

}
