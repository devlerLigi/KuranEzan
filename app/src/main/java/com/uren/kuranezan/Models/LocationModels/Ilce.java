package com.uren.kuranezan.Models.LocationModels;

import java.io.Serializable;

public class Ilce implements Serializable
{
    private String IlceID;

    private String IlceAdi;

    private String IlceAdiEn;

    public String getIlceID ()
    {
        return IlceID;
    }

    public void setIlceID (String IlceID)
    {
        this.IlceID = IlceID;
    }

    public String getIlceAdi ()
    {
        return IlceAdi;
    }

    public void setIlceAdi (String IlceAdi)
    {
        this.IlceAdi = IlceAdi;
    }

    public String getIlceAdiEn ()
    {
        return IlceAdiEn;
    }

    public void setIlceAdiEn (String IlceAdiEn)
    {
        this.IlceAdiEn = IlceAdiEn;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [IlceID = "+IlceID+", IlceAdi = "+IlceAdi+", IlceAdiEn = "+IlceAdiEn+"]";
    }
}