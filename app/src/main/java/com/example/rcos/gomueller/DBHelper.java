package com.example.rcos.gomueller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public String nameExercise;

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + Exercise.TABLE + "("+Exercise.keyId
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Exercise.keyName + " TEXT, "
               + Exercise.keyWeight + " INTEGER, "
                + Exercise.keyNumber + " INTEGER);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Exercise.TABLE);
        onCreate(db);

    }


}
