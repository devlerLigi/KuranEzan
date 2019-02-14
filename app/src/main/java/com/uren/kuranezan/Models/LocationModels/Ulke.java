package com.uren.kuranezan.Models.LocationModels;

import com.uren.kuranezan.Models.QuranModels.Edition;
import com.uren.kuranezan.Models.QuranModels.Surahs;

public class Ulke
{
    private String UlkeAdiEn;

    private String UlkeID;

    private String UlkeAdi;

    public String getUlkeAdiEn ()
    {
        return UlkeAdiEn;
    }

    public void setUlkeAdiEn (String UlkeAdiEn)
    {
        this.UlkeAdiEn = UlkeAdiEn;
    }

    public String getUlkeID ()
    {
        return UlkeID;
    }

    public void setUlkeID (String UlkeID)
    {
        this.UlkeID = UlkeID;
    }

    public String getUlkeAdi ()
    {
        return UlkeAdi;
    }

    public void setUlkeAdi (String UlkeAdi)
    {
        this.UlkeAdi = UlkeAdi;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [UlkeAdiEn = "+UlkeAdiEn+", UlkeID = "+UlkeID+", UlkeAdi = "+UlkeAdi+"]";
    }
}