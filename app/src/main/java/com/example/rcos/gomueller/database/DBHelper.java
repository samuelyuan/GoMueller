package com.example.rcos.gomueller.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rcos.gomueller.model.Exercise;
import com.example.rcos.gomueller.model.Weight;

public class DBHelper extends SQLiteOpenHelper {

    public String nameExercise;

    private static final int DATABASE_VERSION = 9;

    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_WEIGHT_TABLE = "CREATE TABLE " + Weight.TABLE + "(" + Weight._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + Weight.keyWeight + " INTEGER, " +
                Weight.keyDate + " TEXT);";
        
        final String CREATE_EXERCISE_TABLE = "CREATE TABLE " + Exercise.TABLE + "("+Exercise._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Exercise.keyName + " TEXT, "
                + Exercise.keyWeight + " INTEGER, "
                + Exercise.keyAttributeName + " TEXT, "
                + Exercise.keyDate + " TEXT, "
                + Exercise.keyNotes + " TEXT );";

        db.execSQL(CREATE_EXERCISE_TABLE);
        db.execSQL(CREATE_WEIGHT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Exercise.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Weight.TABLE);
        onCreate(db);

    }


}
