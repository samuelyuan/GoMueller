package com.example.rcos.gomueller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ExerciseCRUD {

    private DBHelper dbHelper;

    public ExerciseCRUD(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Exercise exercise) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Exercise.keyName, exercise.name);
        values.put(Exercise.keyWeight, exercise.weight);
        values.put(Exercise.keyNumber, exercise.number);

        long exercise_id = db.insert(Exercise.TABLE, null, values);


        db.close();
        return (int) exercise_id;
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
                +" WHERE " + Exercise.keyName + " = " + "\"" + item + "\"";
        ArrayList<String> exerciseDetail = new ArrayList<String>();
        String w8;
        String numSet;
        String detail;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
               w8 = cursor.getString(cursor.getColumnIndex(Exercise.keyWeight));
               numSet = cursor.getString(cursor.getColumnIndex(Exercise.keyNumber));
               detail = "Weight: " + w8 + "      Set: " + numSet;
               exerciseDetail.add(detail);
            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return  exerciseDetail;
    }
}
