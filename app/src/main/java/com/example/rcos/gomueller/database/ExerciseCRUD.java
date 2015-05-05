package com.example.rcos.gomueller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rcos.gomueller.ParseData;
import com.example.rcos.gomueller.UnitDate;
import com.example.rcos.gomueller.WeightUnit;
import com.example.rcos.gomueller.model.Exercise;
import com.example.rcos.gomueller.model.Weight;

import java.util.ArrayList;
import java.util.Collections;

public class ExerciseCRUD {

    private DBHelper dbHelper;
    private Context currentContext;

    public ExerciseCRUD(Context context) {
        dbHelper = new DBHelper(context);
        this.currentContext = context;
    }

    //Add exercise
    public int insert(Exercise exercise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Exercise.keyName, exercise.activityName);
        values.put(Exercise.keyAttributeName, exercise.attributeName);
        values.put(Exercise.keyWeight, exercise.weight);
        values.put(Exercise.keyDate, exercise.date);
        values.put(Exercise.keyNotes, exercise.notes);

        long exercise_id = db.insert(Exercise.TABLE, null, values);

        db.close();
        return (int) exercise_id;
    }

    //Edit exercise
    public void edit(Exercise exercise, Exercise oldExercise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selectQuery = "SELECT * "
                + " FROM " + Exercise.TABLE
                + " WHERE " + Exercise.keyName + "=\"" + oldExercise.activityName + "\""
                + "and " + Exercise.keyWeight + "=" + oldExercise.weight;

        Cursor cursor = db.rawQuery(selectQuery, null);

        //Read every row in the database
        if (cursor.moveToFirst()) {
            do {
                long rowid = cursor.getLong(cursor.getColumnIndex("_id"));
                ContentValues values = new ContentValues();
                values.put(Exercise.keyWeight, exercise.weight);
                values.put(Exercise.keyDate, exercise.date);
                values.put(Exercise.keyNotes, exercise.notes);

                db.update(Exercise.TABLE, values, "_id = " + rowid, null);

                break;
            } while (cursor.moveToNext());
        }

        db.close();
        return;
    }

    //Add weight
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
    //(NOTE: this function might remove two strings with the same weight and number of sets, like two strings with weight: 150, duration: 8)
    public void deleteExercise(String exerciseName, String currentDetailStr) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String weightStr = ParseData.getAttributeValue(currentDetailStr);

        //convert data if necessary since the data is in the metric system
        if (WeightUnit.settingsUseImperial(currentContext)) {
            weightStr = WeightUnit.convertToMetric(weightStr);
        }

        db.delete(Exercise.TABLE, Exercise.keyName + "=\"" + exerciseName + "\""
                        + "and " + Exercise.keyWeight + "=" + weightStr, null);

        db.close();
    }

    public void deleteWeight(String currentDetailStr) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //parse the string
        String weightStr = ParseData.getAttributeValue(currentDetailStr);
        String dateStr = ParseData.getDate(currentDetailStr);

        //the data in string might be in standard system
        // database uses metric, so convert
        if (WeightUnit.settingsUseImperial(currentContext)) {
            weightStr = WeightUnit.convertToMetric(weightStr);
        }

        db.delete(Weight.TABLE, Weight.keyDate + "='" + dateStr + "'"
                + " and " + Weight.keyWeight + "=" + weightStr, null);

        db.close();
    }

    public String getAttributeName(String exerciseName)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " + Exercise.keyAttributeName
                + " FROM " + Exercise.TABLE
                + " WHERE " + Exercise.keyName + " = " + "\"" + exerciseName + "\"";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String attributeName = "";

        //Read every row in the database
        if (cursor.moveToFirst()) {
            do {
                attributeName = cursor.getString(cursor.getColumnIndex(Exercise.keyAttributeName));
            } while (cursor.moveToNext());
        }

        return attributeName;
    }

    //For TrackExerciseActivity
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

    //For ShowDetailActivity (showing exercises)
    public ArrayList<String> getExerciseDetail(String item) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " + Exercise.keyWeight  + " , " + Exercise.keyDate + " , " + Exercise.keyNotes
                + " FROM " + Exercise.TABLE
                + " WHERE " + Exercise.keyName + " = " + "\"" + item + "\"";
        ArrayList<String> exerciseDetail = new ArrayList<String>();
        String detailStr;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Set the weight's units depending on user's preferences
        String whichLabel = WeightUnit.getWhichLabel(currentContext);

        //Read every row in the database
        if (cursor.moveToFirst()) {
            do {
                detailStr = getDetailStr(cursor, whichLabel);
                exerciseDetail.add(detailStr);
            } while (cursor.moveToNext());
        }

        Collections.sort(exerciseDetail);

        cursor.close();
        db.close();

        return  exerciseDetail;
    }

    //For ShowDetailActivity (showing weight)
    public ArrayList<String> getWeightDetail() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " + Weight.keyWeight + " , " + Weight.keyDate
                + " FROM " + Weight.TABLE;
        ArrayList<String> weightHistory = new ArrayList<String>();
        String weightStr, dateMeasured;
        String detailStr;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Set the weight's units depending on user's preferences
        String whichLabel = WeightUnit.getWhichLabel(currentContext);

        //Read every row in the database
        if (cursor.moveToFirst()) {
            do {
                dateMeasured = cursor.getString(cursor.getColumnIndex(Weight.keyDate));
                weightStr = cursor.getString(cursor.getColumnIndex(Weight.keyWeight));

                //convert to the standard system since data is in the metric system
                if (WeightUnit.settingsUseImperial(currentContext)) {
                    weightStr = WeightUnit.convertToImperial(weightStr);
                }

                dateMeasured = UnitDate.convertFormatFromDisplayToSorted(dateMeasured);

                detailStr = dateMeasured + " : Weight: " + weightStr + " " + whichLabel;
                weightHistory.add(detailStr);
            } while (cursor.moveToNext());
        }

        Collections.sort(weightHistory);

        cursor.close();
        db.close();

        return weightHistory;
    }

    public String getDetailStr(Cursor cursor, String whichLabel)
    {
        String detailStr = "";

        String weightStr = cursor.getString(cursor.getColumnIndex(Exercise.keyWeight));
        String date = cursor.getString(cursor.getColumnIndex(Exercise.keyDate));
        String notes = cursor.getString(cursor.getColumnIndex(Exercise.keyNotes));

        //convert to the standard system since data is in the metric system
        if (WeightUnit.settingsUseImperial(currentContext)) {
            weightStr = WeightUnit.convertToImperial(weightStr);
        }

        //convert date from MM/dd/yyyy to yyyy/MM/dd
        //this makes sorting dates easier
        detailStr = UnitDate.convertFormatFromDisplayToSorted(date);

        if (Integer.parseInt(weightStr) > 0)
            detailStr += " Weight: " + weightStr + " " + whichLabel;

        detailStr += " Notes: " + notes;

        return detailStr;
    }
}
