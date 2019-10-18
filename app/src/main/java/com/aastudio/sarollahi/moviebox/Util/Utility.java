package com.aastudio.sarollahi.moviebox.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Utility {
    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }


}
