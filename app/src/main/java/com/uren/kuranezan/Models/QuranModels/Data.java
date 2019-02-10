package com.uren.kuranezan.Models.QuranModels;

public class Data
{
    private Edition edition;

    private Surahs[] surahs;

    public Edition getEdition ()
    {
        return edition;
    }

    public void setEdition (Edition edition)
    {
        this.edition = edition;
    }

    public Surahs[] getSurahs ()
    {
        return surahs;
    }

    public void setSurahs (Surahs[] surahs)
    {
        this.surahs = surahs;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [edition = "+edition.toString() +", surahs = "+surahs.toString()+"]";
    }
}