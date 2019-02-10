package com.uren.kuranezan.MainFragments.TabKuran.JavaClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.Models.QuranModels.Quran;
import com.uren.kuranezan.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class QuranAsyncProcess extends AsyncTask<Context, Void, Quran> {

    private OnEventListener<Quran> mCallBack;
    private Exception mException;
    private int requestType;


    public QuranAsyncProcess(OnEventListener callback, int requestType) {
        mCallBack = callback;
        this.requestType = requestType;
    }

    @Override
    protected Quran doInBackground(Context... contexts) {

        Context context = contexts[0];

        if (requestType == 1) {
            return setQuranOriginal(context);
        } else if (requestType == 2) {
            return setQuranTransliteration(context);
        } else if (requestType == 3) {
            return setQuranTranslation(context);
        } else {
            //do nothing..
        }

        return null;
    }


    private Quran setQuranOriginal(Context context) {
        InputStream raw = context.getResources().openRawResource(R.raw.quran_uthmani);
        Reader rd = new BufferedReader(new InputStreamReader(raw));
        // Now do the magic.
        return new Gson().fromJson(rd, Quran.class);
    }

    private Quran setQuranTransliteration(Context context) {
        InputStream raw = context.getResources().openRawResource(R.raw.quran_transliteration_tr);
        Reader rd = new BufferedReader(new InputStreamReader(raw));
        // Now do the magic.
        return new Gson().fromJson(rd, Quran.class);
    }

    private Quran setQuranTranslation(Context context) {
        InputStream raw = context.getResources().openRawResource(R.raw.quran_translation_tr_diyanet);
        Reader rd = new BufferedReader(new InputStreamReader(raw));
        // Now do the magic.
        return new Gson().fromJson(rd, Quran.class);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mCallBack != null) {
            mCallBack.onTaskContinue();
        }

    }

    @Override
    protected void onPostExecute(Quran quran) {
        super.onPostExecute(quran);

        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(quran);
            } else {
                mCallBack.onFailure(mException);
            }
        }

    }
}