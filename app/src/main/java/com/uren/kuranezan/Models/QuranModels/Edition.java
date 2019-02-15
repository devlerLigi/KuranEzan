package com.uren.kuranezan.Models.QuranModels;

import java.io.Serializable;

public class Edition implements Serializable
{
    private String identifier;

    private String englishName;

    private String name;

    private String format;

    private String language;

    private String type;

    public String getIdentifier ()
    {
        return identifier;
    }

    public void setIdentifier (String identifier)
    {
        this.identifier = identifier;
    }

    public String getEnglishName ()
    {
        return englishName;
    }

    public void setEnglishName (String englishName)
    {
        this.englishName = englishName;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFormat ()
    {
        return format;
    }

    public void setFormat (String format)
    {
        this.format = format;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [identifier = "+identifier+", englishName = "+englishName+", name = "+name+", format = "+format+", language = "+language+", type = "+type+"]";
    }
}