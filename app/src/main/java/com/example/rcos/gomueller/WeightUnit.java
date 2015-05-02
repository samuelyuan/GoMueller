package com.example.rcos.gomueller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class WeightUnit {
    private static final double KILOGRAM_TO_POUND = 2.204623;
    private static final double POUND_TO_KILOGRAM = 0.453592;

    public static String convertToImperial(String weightStr) {
        return String.valueOf(Math.round(Double.parseDouble(weightStr) * WeightUnit.KILOGRAM_TO_POUND));
    }

    public static int convertToImperial(int weight) {
        return (int) (Math.round((double) weight * WeightUnit.KILOGRAM_TO_POUND));
    }

    public static String convertToMetric(String weightStr) {
        return String.valueOf(Math.round(Double.parseDouble(weightStr) * WeightUnit.POUND_TO_KILOGRAM));
    }

    public static int convertToMetric(int weight) {
        return (int)Math.round((double)weight * WeightUnit.POUND_TO_KILOGRAM);
    }

    public static boolean settingsUseMetric(Context context)
    {
        return (getWhichSystem(context).equals("metric"));
    }

    public static boolean settingsUseImperial(Context context)
    {
        return (getWhichSystem(context).equals("imperial"));
    }

    public static String getWhichLabel(Context context) {
        String whichSystem = getWhichSystem(context);
        if (settingsUseMetric(context))
            return "kgs";
        else if (settingsUseImperial(context))
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
