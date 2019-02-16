package com.uren.kuranezan.Models.PrayerTimeModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrayerTimes implements Serializable {

    private String MiladiTarihKisaIso8601;

    private String MiladiTarihKisa;

    private String GunesDogus;

    private String HicriTarihKisa;

    private String KibleSaati;

    private String Ikindi;

    private String MiladiTarihUzunIso8601;

    private String HicriTarihUzun;

    private String Imsak;

    private String Gunes;

    private String MiladiTarihUzun;

    private String Aksam;

    private String Ogle;

    private String Yatsi;

    private String GunesBatis;

    private String AyinSekliURL;

    public String getMiladiTarihKisaIso8601() {
        return MiladiTarihKisaIso8601;
    }

    public void setMiladiTarihKisaIso8601(String MiladiTarihKisaIso8601) {
        this.MiladiTarihKisaIso8601 = MiladiTarihKisaIso8601;
    }

    public String getMiladiTarihKisa() {
        return MiladiTarihKisa;
    }

    public void setMiladiTarihKisa(String MiladiTarihKisa) {
        this.MiladiTarihKisa = MiladiTarihKisa;
    }

    public String getGunesDogus() {
        return GunesDogus;
    }

    public void setGunesDogus(String GunesDogus) {
        this.GunesDogus = GunesDogus;
    }

    public String getHicriTarihKisa() {
        return HicriTarihKisa;
    }

    public void setHicriTarihKisa(String HicriTarihKisa) {
        this.HicriTarihKisa = HicriTarihKisa;
    }

    public String getKibleSaati() {
        return KibleSaati;
    }

    public void setKibleSaati(String KibleSaati) {
        this.KibleSaati = KibleSaati;
    }

    public String getIkindi() {
        return Ikindi;
    }

    public void setIkindi(String Ikindi) {
        this.Ikindi = Ikindi;
    }

    public String getMiladiTarihUzunIso8601() {
        return MiladiTarihUzunIso8601;
    }

    public void setMiladiTarihUzunIso8601(String MiladiTarihUzunIso8601) {
        this.MiladiTarihUzunIso8601 = MiladiTarihUzunIso8601;
    }

    public String getHicriTarihUzun() {
        return HicriTarihUzun;
    }

    public void setHicriTarihUzun(String HicriTarihUzun) {
        this.HicriTarihUzun = HicriTarihUzun;
    }

    public String getImsak() {
        return Imsak;
    }

    public void setImsak(String Imsak) {
        this.Imsak = Imsak;
    }

    public String getGunes() {
        return Gunes;
    }

    public void setGunes(String Gunes) {
        this.Gunes = Gunes;
    }

    public String getMiladiTarihUzun() {
        return MiladiTarihUzun;
    }

    public void setMiladiTarihUzun(String MiladiTarihUzun) {
        this.MiladiTarihUzun = MiladiTarihUzun;
    }

    public String getAksam() {
        return Aksam;
    }

    public void setAksam(String Aksam) {
        this.Aksam = Aksam;
    }

    public String getOgle() {
        return Ogle;
    }

    public void setOgle(String Ogle) {
        this.Ogle = Ogle;
    }

    public String getYatsi() {
        return Yatsi;
    }

    public void setYatsi(String Yatsi) {
        this.Yatsi = Yatsi;
    }

    public String getGunesBatis() {
        return GunesBatis;
    }

    public void setGunesBatis(String GunesBatis) {
        this.GunesBatis = GunesBatis;
    }

    public String getAyinSekliURL() {
        return AyinSekliURL;
    }

    public void setAyinSekliURL(String AyinSekliURL) {
        this.AyinSekliURL = AyinSekliURL;
    }

    @Override
    public String toString() {
        return "ClassPojo [MiladiTarihKisaIso8601 = " + MiladiTarihKisaIso8601 + ", MiladiTarihKisa = " + MiladiTarihKisa + ", GunesDogus = " + GunesDogus + ", HicriTarihKisa = " + HicriTarihKisa + ", KibleSaati = " + KibleSaati + ", Ikindi = " + Ikindi + ", MiladiTarihUzunIso8601 = " + MiladiTarihUzunIso8601 + ", HicriTarihUzun = " + HicriTarihUzun + ", Imsak = " + Imsak + ", Gunes = " + Gunes + ", MiladiTarihUzun = " + MiladiTarihUzun + ", Aksam = " + Aksam + ", Ogle = " + Ogle + ", Yatsi = " + Yatsi + ", GunesBatis = " + GunesBatis + ", AyinSekliURL = " + AyinSekliURL + "]";
    }
}
			