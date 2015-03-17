package com.example.rcos.gomueller;

import android.provider.BaseColumns;

public class Exercise implements BaseColumns {
    //labels table name
    public static final String TABLE = "Exercise";

    //labels table column names
    public static final String keyName = "name";
    public static final String keyWeight = "weight";
    public static final String keyNumber = "number";


    //properties help to keep data
    public int weight;
    public int number;
    public String activityName;

    public String getName() {
        return activityName;
    }

}
