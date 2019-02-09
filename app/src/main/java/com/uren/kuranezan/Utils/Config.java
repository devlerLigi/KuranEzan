package com.uren.kuranezan.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Config {

  public static final int FONT_QALAM_MAJEED = 0;
  public static final int FONT_HAFS = 1;
  public static final int FONT_NOOREHUDA = 2;
  public static final int FONT_ME_QURAN = 3;
  public static final int FONT_MAX = 3;

  //shared preferences KEYS
  public static final String LANG = "lang";
  public static final String SHOW_TRANSLATION = "showTranslation";
  public static final String SHOW_TRANSLITERATION = "showTransliteration";
  public static final String FONT_ARABIC = "fontArabic";
  public static final String FONT_SIZE_ARABIC = "fontSizeArabic";
  public static final String FONT_SIZE_TRANSLITERATION = "fontSizeTransliteration";
  public static final String FONT_SIZE_TRANSLATION = "fontSizeTranslation";

  //default values
  public static final String defaultLang = "tr";
  public static final boolean defaultShowTransliteration = true;
  public static final boolean defaultShowTranslation = true;
  public static final String defaultFontArabic = "PDMS_IslamicFont.ttf";
  public static final String defaultFontSizeArabic = "30";
  public static final String defaultFontSizeTransliteration = "30";
  public static final String defaultFontSizeTranslation = "30";

  // current variables-bunlar uzerınden ilerlenmeli
  public static String lang;
  public static boolean showTransliteration;
  public static boolean showTranslation;
  public static String fontArabic;
  public static String fontSizeArabic;
  public static String fontSizeTransliteration;
  public static String fontSizeTranslation;

  public void load(Context context) {
    Log.d("Config", "Load");
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    try {
      loadDefault();
      lang = sp.getString(Config.LANG, Config.defaultLang);
      showTransliteration = sp.getBoolean(Config.SHOW_TRANSLITERATION, Config.defaultShowTransliteration);
      showTranslation = sp.getBoolean(Config.SHOW_TRANSLATION, Config.defaultShowTranslation);
      fontArabic = sp.getString(Config.FONT_ARABIC, Config.defaultFontArabic);
      fontSizeArabic = sp.getString(Config.FONT_SIZE_ARABIC, Config.defaultFontSizeArabic);
      fontSizeTransliteration = sp.getString(Config.FONT_SIZE_TRANSLITERATION, Config.defaultFontSizeTransliteration);
      fontSizeTranslation = sp.getString(Config.FONT_SIZE_TRANSLATION, Config.defaultFontSizeTranslation);
      Log.d("Config", "Loading Custom");
    } catch (Exception e) {
      loadDefault();
      Log.d("Config", "Exception Loading Defaults");
    }
  }

  public void loadDefault() {
    lang = defaultLang;
    showTransliteration = defaultShowTransliteration;
    showTranslation=defaultShowTranslation;
    fontArabic=defaultFontArabic;
    fontSizeArabic=defaultFontSizeArabic;
    fontSizeTransliteration=defaultFontSizeTransliteration;
    fontSizeTranslation=defaultFontSizeTranslation;
  }

  /*public void save(Context context) {
      Log.d("Config","Save");
      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
      SharedPreferences.Editor ed = sp.edit();
      ed.clear();
      ed.putString(LANG, lang);
      ed.putBoolean(SHOW_TRANSLATION, showTranslation);
      ed.putBoolean(WORD_BY_WORD, wordByWord);
      ed.putBoolean(KEEP_SCREEN_ON, keepScreenOn);
      ed.putString(FONT_SIZE_ARABIC, "" + fontSizeArabic);
      ed.putString(FONT_SIZE_TRANSLATION, "" + fontSizeTranslation);
      ed.commit();
  }*/
  private int getStringInt(SharedPreferences sp, String key, int defValue) {
    return Integer.parseInt(sp.getString(key, Integer.toString(defValue)));
  }

  /*  public boolean loadFont() {
      if (loadedFont != Config.fontArabic) {
          String name;
          switch (config.fontArabic) {
              case Config.FONT_NASKH:
                  name = "naskh.otf";
                  break;
              case Config.FONT_NOOREHUDA:
                  name = "noorehuda.ttf";
                  break;
              case Config.FONT_ME_QURAN:
                  name = "me_quran.ttf";
                  break;
              default:
                  name = "qalam.ttf";
          }
          try {
              NativeRenderer.loadFont(getAssets().open(name));
              loadedFont = config.fontArabic;
          } catch (IOException e) {
              e.printStackTrace();
              loadedFont = -1;
              return false;
          }
      }
      return true;
  }*/

}
