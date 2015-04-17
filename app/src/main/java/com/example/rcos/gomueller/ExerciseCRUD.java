package com.example.rcos.gomueller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
        values.put(Exercise.keyWeight, exercise.weight);
        values.put(Exercise.keyNumber, exercise.number);

        long exercise_id = db.insert(Exercise.TABLE, null, values);

        db.close();
        return (int) exercise_id;
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
        String weightStr = "0", timeSpent = "0";

        String[] splitString = currentDetailStr.split(" ");
        for (int i = 0; i < splitString.length - 1; i++)
        {
            if (splitString[i].equals("Weight:"))
                weightStr = String.valueOf(splitString[i+1]);
            else if (splitString[i].equals("Duration:"))
                timeSpent = String.valueOf(splitString[i+1]);

        }

        //convert data if necessary since the data is in the metric system
        if (WeightUnit.isImperial(currentContext)) {
            weightStr = String.valueOf(Math.round (Double.parseDouble(weightStr) * WeightUnit.POUND_TO_KILOGRAM));
        }

        db.delete(Exercise.TABLE, Exercise.keyName + "=\"" + exerciseName + "\""
                        + "and " + Exercise.keyWeight + "=" + weightStr +
                        " and " + Exercise.keyNumber + "=" + timeSpent, null);

        db.close();
    }

    public void deleteWeight(String currentDetailStr) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //parse the string
        String weightStr = "0";
        String[] splitString = currentDetailStr.split(" ");

        String dateStr = splitString[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date tempDate = formatter.parse(dateStr);
            formatter = new SimpleDateFormat("MM/dd/yyyy");
            dateStr = formatter.format(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < splitString.length - 1; i++)
        {
            if (splitString[i].equals("Weight:")) {
                weightStr = String.valueOf(splitString[i + 1]);
                break;
            }
        }

        //convert data if necessary since the data is in the metric system
        if (WeightUnit.isImperial(currentContext)) {
            weightStr = String.valueOf(Math.round (Double.parseDouble(weightStr) * WeightUnit.POUND_TO_KILOGRAM));
        }

        db.delete(Weight.TABLE, Weight.keyDate + "='" + dateStr + "'"
                + " and " + Weight.keyWeight + "=" + weightStr, null);

        db.close();
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
        String selectQuery = "SELECT " + Exercise.keyWeight + " , " + Exercise.keyNumber
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
                if (WeightUnit.isImperial(currentContext)) {
                    weightStr = String.valueOf(Math.round (Double.parseDouble(weightStr) * WeightUnit.KILOGRAM_TO_POUND));
                }


                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date testDate = null;
                try {
                    testDate = sdf.parse(dateMeasured);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                dateMeasured = formatter.format(testDate);

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
        String timeSpent = cursor.getString(cursor.getColumnIndex(Exercise.keyNumber));

        //convert to the standard system since data is in the metric system
        if (WeightUnit.isImperial(currentContext)) {
            weightStr = String.valueOf(Math.round (Double.parseDouble(weightStr) * WeightUnit.KILOGRAM_TO_POUND));
        }

        if (Integer.parseInt(weightStr) > 0)
            detailStr += "Weight: " + weightStr + " " + whichLabel;

        //only display time length if needed
        if (Integer.parseInt(timeSpent) > 0) {
            if (Integer.parseInt(weightStr) > 0)
                detailStr += ",      Duration: " + timeSpent + " mins";
            else
                detailStr += "Duration: " + timeSpent + " mins";
        }

        return detailStr;
    }
}
