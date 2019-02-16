package com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses;

import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;
import com.uren.kuranezan.Models.QuranModels.Quran;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.Reader;

import static com.uren.kuranezan.Constants.StringConstants.PRAYER_TIMES_INTERNAL_FILE_PREFIX;

public class PrayerAsyncProcess extends AsyncTask<Context, Void, PrayerTimes[]> {

    private OnEventListener<PrayerTimes[]> mCallBack;
    private Exception mException;

    public PrayerAsyncProcess(OnEventListener callback) {
        mCallBack = callback;
    }

    @Override
    protected PrayerTimes[] doInBackground(Context... contexts) {

        long lStartTime = System.currentTimeMillis();

        Context context = contexts[0];
        ObjectMapper mapper = new ObjectMapper();
        PrayerTimes[] prayerTimes = setPrayerTimes(context, mapper);

        long lEndTime = System.currentTimeMillis();
        long output = lEndTime - lStartTime;

        return prayerTimes;
    }

    private PrayerTimes[] setPrayerTimes(Context context, ObjectMapper mapper) {

        String fileName = getInternalFileName("");
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            Reader rd = new BufferedReader(new InputStreamReader(fis));
            // Now do the magic.
            //return new Gson().fromJson(rd, Quran.class);

            try {
                return mapper.readValue(rd, PrayerTimes[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

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
    protected void onPostExecute(PrayerTimes[] prayerTimes) {
        super.onPostExecute(prayerTimes);

        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(prayerTimes);
            } else {
                mCallBack.onFailure(mException);
            }
        }

    }

    private String getInternalFileName(String identifier) {
        return PRAYER_TIMES_INTERNAL_FILE_PREFIX + getFileIdentifier(identifier) + ".json";
    }

    private String getFileIdentifier(String identifier) {
        //return identifier.replace(".", "");
        return "";
    }
}