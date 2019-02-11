package com.uren.kuranezan.MainFragments.TabKuran.JavaClasses;

import android.content.Context;

import com.uren.kuranezan.Interfaces.OptionsCallback;
import com.uren.kuranezan.MainFragments.BaseFragment;
import com.uren.kuranezan.MainFragments.TabKuran.SubFragments.OptionsFragment;

import java.util.ArrayList;
import java.util.List;

public class OptionsHelper {

    public static class OptionsClicked {

        private static OptionsClicked instance = null;
        private static List<OptionsCallback> optionsCallbackList;

        private static BaseFragment.FragmentNavigation fragmentNavigation;
        private static int numberOfCallback;

        public OptionsClicked() {
            optionsCallbackList = new ArrayList<OptionsCallback>();
            numberOfCallback = -1;
        }

        public static OptionsClicked getInstance() {
            if (instance == null)
                instance = new OptionsClicked();

            return instance;
        }

        public void setSinglePostItems(BaseFragment.FragmentNavigation fragmentNavigation) {

            OptionsClicked.fragmentNavigation = fragmentNavigation;

        }

        public void startOptionsProcess() {
            if (fragmentNavigation != null) {
                numberOfCallback++;
                fragmentNavigation.pushFragment(OptionsFragment.newInstance(numberOfCallback));
            }
        }

        public void setProfileRefreshCallback(OptionsCallback optionsCallback) {
            optionsCallbackList.add(optionsCallback);
        }

        public void onLanguageChanged(int _numberOfCallback, String language){
            optionsCallbackList.get(_numberOfCallback).onLanguageChanged(language);
        }
        public void onShowTransliterationChanged(int _numberOfCallback, boolean isShow){
            optionsCallbackList.get(_numberOfCallback).onShowTransliterationChanged(isShow);
        }
        public void onShowTranslationChanged(int _numberOfCallback, boolean isShow){
            optionsCallbackList.get(_numberOfCallback).onShowTranslationChanged(isShow);
        }
        public void onFontArabicChanged(int _numberOfCallback, String fontType){
            optionsCallbackList.get(_numberOfCallback).onFontArabicChanged(fontType);
        }
        public void onFontSizeArabicChanged(int _numberOfCallback, int fontSize){
            optionsCallbackList.get(_numberOfCallback).onFontSizeArabicChanged(fontSize);
        }
        public void onFontSizeTransliterationChanged(int _numberOfCallback, int fontSize){
            optionsCallbackList.get(_numberOfCallback).onFontSizeTransliterationChanged(fontSize);
        }
        public void onFontSizeTranslationChanged(int _numberOfCallback, int fontSize){
            optionsCallbackList.get(_numberOfCallback).onFontSizeTranslationChanged(fontSize);
        }

    }

}
