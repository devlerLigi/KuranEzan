package com.uren.kuranezan.MainFragments.TabNamazVakti.JavaClasses;

import com.uren.kuranezan.Interfaces.NamazVaktiCallback;

import java.util.ArrayList;
import java.util.List;

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
