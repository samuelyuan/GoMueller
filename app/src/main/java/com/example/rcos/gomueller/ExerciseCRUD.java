package com.example.rcos.gomueller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.ArrayList;

public class ExerciseCRUD {

    private DBHelper dbHelper;
    private Context currentContext;

    public ExerciseCRUD(Context context) {
        dbHelper = new DBHelper(context);
        this.currentContext = context;
    }

    public int insert(Exercise exercise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Exercise.keyName, exercise.activityName);
        values.put(Exercise.keyWeight, exercise.weight);
        values.put(Exercise.keyNumber, exercise.number);

        long exercise_id = db.insert(Exercise.TABLE, null, values);

        db.close();
        return (int) exercise_id;
    }

    public int insert(Weight weight) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Weight.keyWeight, weight.weight);
        values.put(Weight.keyDate, weight.date);
        
        long weight_id = db.insert(Weight.TABLE, null, values);
        
        db.close();
        return (int)weight_id;
    }
    
    
    //Remove an entry from the database
    //(NOTE: this function might remove two strings with the same weight and number of sets, like two strings with weight: 150, numSets: 8)
    public void delete(String exerciseName, String currentDetailStr) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "SELECT " + Exercise.keyWeight + " , " + Exercise.keyNumber
                + " FROM " + Exercise.TABLE
                + " WHERE " + Exercise.keyName + " = " + "\"" + exerciseName + "\"";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String weightStr, timeSpent;
        String detail;

        //Go through each rows
        if (cursor.moveToFirst()) {
            do {
                //form detail string
                weightStr = cursor.getString(cursor.getColumnIndex(Exercise.keyWeight));
                timeSpent = cursor.getString(cursor.getColumnIndex(Exercise.keyNumber));
                detail = "Weight: " + weightStr + " kgs,      Duration: " + timeSpent + " mins";

                //delete this entry
                if (detail.equals(currentDetailStr))
                {
                    db.delete(Exercise.TABLE, Exercise.keyWeight + "=" + weightStr + " and " + Exercise.keyNumber + "=" + timeSpent, null);
                }

                //delete the exercise if needed
                if (cursor.getCount() == 0) {
                    db.delete(Exercise.TABLE, Exercise.keyName + " = " + "\"" + exerciseName + "\"", null);
                    break;
                }

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
    }

    public ArrayList<String> getExerciseArray() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " + Exercise.keyName + " FROM " + Exercise.TABLE;

        ArrayList<String> exerciseArrayList = new ArrayList<String>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String exercise = cursor.getString(cursor.getColumnIndex(Exercise.keyName));
                if (exerciseArrayList.contains(exercise) == false) {
                    exerciseArrayList.add(exercise);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return exerciseArrayList;
    }

    public ArrayList<String> getExerciseDetail(String item) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " + Exercise.keyWeight + " , " + Exercise.keyNumber
                + " FROM " + Exercise.TABLE
                + " WHERE " + Exercise.keyName + " = " + "\"" + item + "\"";
        ArrayList<String> exerciseDetail = new ArrayList<String>();
        String weightStr, timeSpent;
        String detail;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Set the weight's units depending on user's preferences
        String whichLabel = "kgs";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.currentContext);
        String defaultValue = this.currentContext.getResources().getString(R.string.pref_units_default);
        String whichSystem = prefs.getString(this.currentContext.getString(R.string.pref_units_key), defaultValue);
        if (whichSystem.equals("metric"))
            whichLabel = "kgs";
        else if (whichSystem.equals("imperial"))
            whichLabel = "lbs";

        //Read every row in the database
        if (cursor.moveToFirst()) {
            do {
                weightStr = cursor.getString(cursor.getColumnIndex(Exercise.keyWeight));
                timeSpent = cursor.getString(cursor.getColumnIndex(Exercise.keyNumber));

                //convert to the standard system since data is in the metric system
                if (whichSystem.equals("imperial")) {
                    weightStr = String.valueOf((int) (Double.parseDouble(weightStr) * 2.204623));
                }

                detail = "Weight: " + weightStr + " " + whichLabel + ",      Duration: " + timeSpent + " mins";
                exerciseDetail.add(detail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return  exerciseDetail;
    }
}
