package com.uren.kuranezan.MainFragments.TabKuran.JavaClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.Models.QuranModels.Quran;
import com.uren.kuranezan.R;
import com.uren.kuranezan.Utils.Config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_ORIGINAL;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_TRANSLATION_DEFAULT;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_TRANSLATION_OTHER;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_QURAN_TRANSLITERATION;
import static com.uren.kuranezan.Constants.StringConstants.INTERNAL_FILE_NAME_PREFIX;

public class QuranAsyncProcess extends AsyncTask<Context, Void, Quran> {

    private OnEventListener<Quran> mCallBack;
    private Exception mException;
    private int requestType;
    private String identifier;

    public QuranAsyncProcess(OnEventListener callback, int requestType, String identifier) {
        mCallBack = callback;
        this.requestType = requestType;
        this.identifier = identifier;
    }

    @Override
    protected Quran doInBackground(Context... contexts) {

        Context context = contexts[0];

        if (requestType == REQUEST_TYPE_QURAN_ORIGINAL) {
            return setQuranOriginal(context);
        } else if (requestType == REQUEST_TYPE_QURAN_TRANSLITERATION) {
            return setQuranTransliteration(context);
        } else if (requestType == REQUEST_TYPE_QURAN_TRANSLATION_DEFAULT) {
            return setQuranTranslation(context);
        } else if (requestType == REQUEST_TYPE_QURAN_TRANSLATION_OTHER) {
            return selectedQuranTranslation(context);
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
        int rawNum;
        if (identifier.equals(Config.defaultTransliterationLang)) {
            rawNum = R.raw.quran_transliteration_tr;
        } else {
            rawNum = R.raw.quran_transliteration_en;
        }

        InputStream raw = context.getResources().openRawResource(rawNum);
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

    private Quran selectedQuranTranslation(Context context) {

        String fileName = getInternalFileName();
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            Reader rd = new BufferedReader(new InputStreamReader(fis));
            // Now do the magic.
            Quran quran = new Gson().fromJson(rd, Quran.class);
            return quran;
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

    private String getInternalFileName() {
        return INTERNAL_FILE_NAME_PREFIX + getFileIdentifier() + ".json";
    }

    private String getFileIdentifier() {
        return identifier.replace('.', '_');
    }
}