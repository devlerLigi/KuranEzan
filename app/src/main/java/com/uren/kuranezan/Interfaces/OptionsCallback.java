package com.uren.kuranezan.Interfaces;

public interface OptionsCallback {

    public void onLanguageChanged(String language);

    public void onShowTransliterationChanged(boolean isShow);

    public void onShowTranslationChanged(boolean isShow);

    public void onFontArabicChanged(String fontType);

    public void onFontSizeArabicChanged(float fontSize);

    public void onFontSizeTransliterationChanged(float fontSize);

    public void onFontSizeTranslationChanged(float fontSize);

}