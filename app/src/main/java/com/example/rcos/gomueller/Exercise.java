package com.example.rcos.gomueller;

import android.provider.BaseColumns;

public class Exercise implements BaseColumns {
    //labels table name
    public static final String TABLE = "Exercise";

    //labels table column names
    public static final String keyName = "name";
    public static final String keyWeight = "weight";
    public static final String keyAttributeName = "attributeName";
    public static final String keyDate = "date";

    //properties help to keep data
    public String activityName;
    public String attributeName;
    public int weight;
    public String date;

    public String getName() {
        return activityName;
    }

}
