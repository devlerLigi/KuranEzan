package com.uren.kuranezan.Models.QuranModels;

import java.io.Serializable;

public class Quran  implements Serializable
{
    private String code;

    private Data data;

    private String status;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", data = "+data.toString()+", status = "+status+"]";
    }
}