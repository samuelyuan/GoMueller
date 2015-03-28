package com.example.rcos.gomueller;

import android.provider.BaseColumns;

/**
 * Created by Yunang on 3/16/15.
 */
public class Weight implements BaseColumns {
    public static final String TABLE = "Weight";

    public static final String keyWeight = "weight";
    public static final String keyDate = "date";
    
    public int weight;
    public String date;
}
