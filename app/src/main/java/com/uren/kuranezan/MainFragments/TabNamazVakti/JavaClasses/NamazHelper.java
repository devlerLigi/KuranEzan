package com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.uren.kuranezan.Interfaces.NamazVaktiCallback;
import com.uren.kuranezan.Models.PrayerTimeModels.PrayerTimes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static com.uren.kuranezan.Constants.StringConstants.PRAYER_TIMES_INTERNAL_FILE_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.PRAYER_TIMES_LIST_URL_PREFIX;
import static com.uren.kuranezan.Constants.StringConstants.SELECTED_COUNTY_CODE;

public class NamazHelper {

    public static class NamazVaktiRefresh {

        private static NamazVaktiRefresh instance = null;
        private static List<NamazVaktiCallback> namazVaktiCallbackList;

        public NamazVaktiRefresh() {
            namazVaktiCallbackList = new ArrayList<NamazVaktiCallback>();
        }

        public static NamazVaktiRefresh getInstance() {
            if (instance == null)
                instance = new NamazVaktiRefresh();

            return instance;
        }

        public void setNamazVaktiCallback(NamazVaktiCallback feedRefreshCallback) {
            namazVaktiCallbackList.add(feedRefreshCallback);
        }

        public static void namazVaktiRefreshStart() {
            if (instance != null) {
                for (int i = 0; i < namazVaktiCallbackList.size(); i++) {
                    namazVaktiCallbackList.get(i).onNamazVaktiRefresh();
                }
            }
        }

    }


}
