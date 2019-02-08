package com.uren.kuranezan.Models;

public class Ayahs
{
    private String number;

    private String hizbQuarter;

    private String ruku;

    private String manzil;

    private String text;

    private String page;

    private String numberInSurah;

    private String juz;

    public String getNumber ()
    {
        return number;
    }

    public void setNumber (String number)
    {
        this.number = number;
    }

    public String getHizbQuarter ()
    {
        return hizbQuarter;
    }

    public void setHizbQuarter (String hizbQuarter)
    {
        this.hizbQuarter = hizbQuarter;
    }

    public String getRuku ()
    {
        return ruku;
    }

    public void setRuku (String ruku)
    {
        this.ruku = ruku;
    }

    public String getManzil ()
    {
        return manzil;
    }

    public void setManzil (String manzil)
    {
        this.manzil = manzil;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getPage ()
    {
        return page;
    }

    public void setPage (String page)
    {
        this.page = page;
    }


    public String getNumberInSurah ()
    {
        return numberInSurah;
    }

    public void setNumberInSurah (String numberInSurah)
    {
        this.numberInSurah = numberInSurah;
    }

    public String getJuz ()
    {
        return juz;
    }

    public void setJuz (String juz)
    {
        this.juz = juz;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [number = "+number+", hizbQuarter = "+hizbQuarter+", ruku = "+ruku+", manzil = "+manzil+", text = "+text+", page = "+page+", numberInSurah = "+numberInSurah+", juz = "+juz+"]";
    }
}
			