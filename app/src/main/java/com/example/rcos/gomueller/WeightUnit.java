package com.example.rcos.gomueller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class WeightUnit
{
    public static final double KILOGRAM_TO_POUND = 2.204623;
    public static final double POUND_TO_KILOGRAM = 0.453592;

    public static boolean isMetric(Context context)
    {
        return (getWhichSystem(context).equals("metric"));
    }

    public static boolean isImperial(Context context)
    {
        return (getWhichSystem(context).equals("imperial"));
    }

    public static String getWhichLabel(Context context) {
        String whichSystem = getWhichSystem(context);
        if (isMetric(context))
            return "kgs";
        else if (isImperial(context))
            return "lbs";

        return "";
    }

    //this should never be used outside of the class
    private static String getWhichSystem(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultValue = context.getResources().getString(R.string.pref_units_default);
        String whichSystem = prefs.getString(context.getString(R.string.pref_units_key), defaultValue);
        return whichSystem;
    }
}
