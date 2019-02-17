package com.uren.kuranezan.Constants;

public class StringConstants {

    public static final String APP_NAME = "Kuran Ezan";

    //Raw files
    public static final String QURAN_TRANSLATION_TR_DIYANET = "quran_trasnlation_tr_diyanet";

    //Prefix
    public static final String INTERNAL_FILE_NAME_PREFIX = "quran_translation_";
    public static final String PRAYER_TIMES_INTERNAL_FILE_PREFIX = "prayertimes_";
    public static final String QURAN_TRANSLATION_URL_PREFIX = "http://api.alquran.cloud/v1/quran/";
    public static final String CITY_LIST_URL_PREFIX = "https://ezanvakti.herokuapp.com/sehirler?ulke=";
    public static final String DISTINCT_LIST_URL_PREFIX = "https://ezanvakti.herokuapp.com/ilceler?sehir=";
    public static final String PRAYER_TIMES_LIST_URL_PREFIX = "https://ezanvakti.herokuapp.com/vakitler?ilce=";
    public static final String MY_ZIKIRS = "MY_ZIKIRS";

    //Dini bilgiler files
    public static final String file_ibadet_nedir = "ibadet_nedir";
    public static final String file_imanin_sartlari = "imanin_sartlari";
    public static final String file_islamin_sartlari = "islamin_sartlari";
    public static final String file_otuziki_farz = "otuziki_farz";
    public static final String file_ellidort_farz = "ellidort_farz";
    public static final String file_farz_nedir = "farz_nedir";
    public static final String file_sunnet_nedir = "sunnet_nedir";
    public static final String file_vacip_nedir = "vacip_nedir";
    public static final String file_kelimei_sahadet = "kelimei_sahadet";
    public static final String file_ezan = "ezan";

    //Cache keys
    public static final String CACHED_CITIES_PREFIX = "cachedCitiesOf";
    public static final String CACHED_COUNTIES_PREFIX = "cachedCountiesOf";

    //Location
    public static String SELECTED_COUNTRY = "";
    public static String SELECTED_CITY = "";
    public static String SELECTED_COUNTY = "";
    public static String SELECTED_COUNTY_CODE = "";

}
