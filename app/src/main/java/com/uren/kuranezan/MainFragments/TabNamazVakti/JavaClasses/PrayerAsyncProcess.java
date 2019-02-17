package com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.uren.kuranezan.Interfaces.OnEventListener;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.Reader;

import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_GET_PRAYER_TIMES_FROM_SERVER;
import static com.uren.kuranezan.Constants.NumericConstants.REQUEST_TYPE_READ_PRAYER_TIMES;
import static com.uren.kuranezan.Constants.StringConstants.PRAYER_TIMES_INTERNAL_FILE_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.PRAYER_TIMES_LIST_URL_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_COUNTY_CODE;

public class PrayerAsyncProcess extends AsyncTask<Context, Void, PrayerTimes[]> {

    private OnEventListener<PrayerTimes[]> mCallBack;
    private Exception mException;
    private int mRequestType;
    private String mIdentifier;

    public PrayerAsyncProcess(OnEventListener callback, int requestType, String identifier) {
        mCallBack = callback;
        mRequestType = requestType;
        mIdentifier = identifier;
    }

    @Override
    protected PrayerTimes[] doInBackground(Context... contexts) {

        Context context = contexts[0];
        ObjectMapper mapper = new ObjectMapper();

        long lStartTime = System.currentTimeMillis();

        if (mRequestType == REQUEST_TYPE_READ_PRAYER_TIMES) {
            return readPrayerTimes(context, mapper);
        } else if (mRequestType == REQUEST_TYPE_GET_PRAYER_TIMES_FROM_SERVER) {
            return getPrayerTimesFromServer(context);
        } else {

        }

        long lEndTime = System.currentTimeMillis();
        long output = lEndTime - lStartTime;

        return null;
    }

    private PrayerTimes[] getPrayerTimesFromServer(Context context) {

        String endpointUrl = PRAYER_TIMES_LIST_URL_PREFIX + mIdentifier;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointUrl)
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
            String content = response.body().string();
            Gson gson = new Gson();
            PrayerTimes[] prayerTimes = gson.fromJson(content, PrayerTimes[].class);
            Log.i("download", "dosya indirildi");
            saveFileToInternal(context, content);
            return prayerTimes;
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return null;
    }

    private void saveFileToInternal(Context context, String file) {

        String fileContents = file;
        FileOutputStream fos = null;
        String fileName = getInternalFileName();

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            String path = context.getFilesDir() + "/" + fileName;
            Log.i("lokale yazildi", path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getInternalFileName() {
        return PRAYER_TIMES_INTERNAL_FILE_PREFIX + getFileIdentifier() + ".json";
    }

    private String getFileIdentifier() {
        return mIdentifier;
    }

    private PrayerTimes[] readPrayerTimes(Context context, ObjectMapper mapper) {

        String fileName = getInternalFileName();
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            Reader rd = new BufferedReader(new InputStreamReader(fis));
            // Now do the magic.
            return new Gson().fromJson(rd, PrayerTimes[].class);
/*
            try {
                PrayerTimes[] prayerTimes = mapper.readValue(rd, PrayerTimes[].class);
                return prayerTimes;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
            */

        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
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


}