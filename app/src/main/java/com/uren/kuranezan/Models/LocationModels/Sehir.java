package com.uren.kuranezan.Models.LocationModels;

import java.io.Serializable;

public class Sehir implements Serializable
{
    private String SehirAdiEn;

    private String SehirID;

    private String SehirAdi;

    public String getSehirAdiEn ()
    {
        return SehirAdiEn;
    }

    public void setSehirAdiEn (String SehirAdiEn)
    {
        this.SehirAdiEn = SehirAdiEn;
    }

    public String getSehirID ()
    {
        return SehirID;
    }

    public void setSehirID (String SehirID)
    {
        this.SehirID = SehirID;
    }

    public String getSehirAdi ()
    {
        return SehirAdi;
    }

    public void setSehirAdi (String SehirAdi)
    {
        this.SehirAdi = SehirAdi;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [SehirAdiEn = "+SehirAdiEn+", SehirID = "+SehirID+", SehirAdi = "+SehirAdi+"]";
    }
}