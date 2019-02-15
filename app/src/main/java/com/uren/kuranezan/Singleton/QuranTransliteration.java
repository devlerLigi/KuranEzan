package com.uren.kuranezan.Singleton;

import android.content.Context;
import android.os.AsyncTask;

import com.uren.kuranezan.Interfaces.CompleteCallback;
import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.MainFragments.TabKuran.JavaClasses.QuranAsyncProcess;
import com.uren.kuranezan.Models.QuranModels.Quran;

import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_TRANSLITERATION;

public class QuranTransliteration {

    private static QuranTransliteration single_instance = null;
    private static CompleteCallback<Quran> mCompleteCallback;
    private static boolean finished = false;
    private Quran quranTransliteration;

    private QuranTransliteration(Context context, String lang) {
        parseJson(context, lang);
    }

    public static QuranTransliteration getInstance(Context context, String lang, CompleteCallback<Quran> completeCallback) {
        if (single_instance == null) {
            mCompleteCallback = completeCallback;
            single_instance = new QuranTransliteration(context, lang);
        }

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
                finished = true;
                setQuranTransliteration(quran);
                mCompleteCallback.onComplete(quran);
            }

            @Override
            public void onFailure(Exception e) {
                mCompleteCallback.onFailed(e);
            }

            @Override
            public void onTaskContinue() {

            }
        }, REQUEST_TYPE_QURAN_TRANSLITERATION, lang);

        quranAsyncProcess.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);

    }

    public static boolean isFinished() {
        return finished;
    }

    public static void setFinished(boolean isFinished) {
        QuranTransliteration.finished = isFinished;
    }

    public static CompleteCallback<Quran> getCompleteCallback() {
        return mCompleteCallback;
    }

    public static void setCompleteCallback(CompleteCallback<Quran> mCompleteCallback) {
        QuranTransliteration.mCompleteCallback = mCompleteCallback;
    }
}
