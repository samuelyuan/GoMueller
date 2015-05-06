package com.example.rcos.gomueller;

import android.content.Context;
import android.content.Intent;

import com.example.rcos.gomueller.database.ExerciseCRUD;

/**
 * Created by yuans on 5/3/2015.
 */
public class IntentParam
{
    private static String getIntentString(Intent intent, String str) { return intent.getStringExtra(str); }
    private static void setIntentString(Intent intent, String str, String value) { intent.putExtra(str, value); }

    public static String getExerciseName(Intent intent) { return getIntentString(intent, "exerciseName"); }
    public static String getExerciseDate(Intent intent) { return getIntentString(intent, "exerciseDate"); }
    public static String getAttributeName(Intent intent) { return getIntentString(intent, "attributeName"); }
    public static String getAttributeValue(Intent intent) { return getIntentString(intent, "attributeValue"); }
    public static String getNotes(Intent intent) { return getIntentString(intent,"notesValue"); }
    public static boolean isTypeExercise(Intent intent) { return getIntentString(intent, "type").equals("exercise"); }
    public static boolean isTypeWeight(Intent intent) { return getIntentString(intent, "type").equals("weight"); }

    public static void setExerciseName(Intent intent, String value) { setIntentString(intent, "exerciseName", value); }
    public static void setExerciseDate(Intent intent, String value) { setIntentString(intent, "exerciseDate", value); }
    public static void setAttributeValue(Intent intent, String value) { setIntentString(intent, "attributeValue", value); }
    public static void setNotes(Intent intent, String value) { setIntentString(intent, "notesValue", value); }
    public static void setTypeExercise(Intent intent) { setIntentString(intent, "type", "exercise"); }
    public static void setTypeWeight(Intent intent) { setIntentString(intent, "type", "weight"); }

    public static void setAttributeName(Intent intent, Context context)
    {
        final ExerciseCRUD crudDetail = new ExerciseCRUD(context);
        String exerciseName = IntentParam.getExerciseName(intent);
        setIntentString(intent, "attributeName", crudDetail.getAttributeName(exerciseName));
    }
}
