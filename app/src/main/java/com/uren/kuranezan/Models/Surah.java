package com.uren.kuranezan.Models;

import java.util.ArrayList;

public class Surah {

    private int number;
    private String name;
    private String englishName;
    private String englishNameTranslation;
    private String revelationType;
    private ArrayList<Ayah> ayahList = new ArrayList<Ayah>();

    public Surah() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getEnglishNameTranslation() {
        return englishNameTranslation;
    }

    public void setEnglishNameTranslation(String englishNameTranslation) {
        this.englishNameTranslation = englishNameTranslation;
    }

    public String getRevelationType() {
        return revelationType;
    }

    public void setRevelationType(String revelationType) {
        this.revelationType = revelationType;
    }

    public ArrayList<Ayah> getAyahList() {
        return ayahList;
    }

    public void setAyahList(ArrayList<Ayah> ayahList) {
        this.ayahList = ayahList;
    }
}
