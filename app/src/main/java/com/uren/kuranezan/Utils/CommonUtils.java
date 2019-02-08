package com.uren.kuranezan.Utils;

import android.content.Context;
import android.content.res.Resources;

import com.uren.kuranezan.R;

import java.util.Random;

public class CommonUtils {

    public static int getRandomColor(Context context) {

        Resources resources = context.getResources();

        int colorList[] = {
                R.color.green,
                R.color.DodgerBlue,
                R.color.Orange,
                R.color.Red,
                R.color.Yellow,
                R.color.MediumSeaGreen,
                R.color.LightBlue,
                R.color.Sienna
        };

        Random rand = new Random();
        return colorList[rand.nextInt(colorList.length)];
    }
}
