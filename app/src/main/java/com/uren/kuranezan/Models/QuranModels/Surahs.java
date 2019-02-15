package com.uren.kuranezan.Models.QuranModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Surahs  implements Serializable
{
    private String number;

    private String englishName;

    private String revelationType;

    private String name;

    private Ayahs[] ayahs;

    private String englishNameTranslation;

    public String getNumber ()
    {
        return number;
    }

    public void setNumber (String number)
    {
        this.number = number;
    }

    public String getEnglishName ()
    {
        return englishName;
    }

    public void setEnglishName (String englishName)
    {
        this.englishName = englishName;
    }

    public String getRevelationType ()
    {
        return revelationType;
    }

    public void setRevelationType (String revelationType)
    {
        this.revelationType = revelationType;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Ayahs[] getAyahs ()
    {
        return ayahs;
    }

    public void setAyahs (Ayahs[] ayahs)
    {
        this.ayahs = ayahs;
    }

    public String getEnglishNameTranslation ()
    {
        return englishNameTranslation;
    }

    public void setEnglishNameTranslation (String englishNameTranslation)
    {
        this.englishNameTranslation = englishNameTranslation;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [number = "+number+", englishName = "+englishName+", revelationType = "+revelationType+", name = "+name+", ayahs = "+ayahs.toString()+", englishNameTranslation = "+englishNameTranslation+"]";
    }
}
