package com.example.rcos.gomueller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UnitDate
{
    private static SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat sortedFormat = new SimpleDateFormat("yyyy/MM/dd");

    public static String convertFormatFromSortedToDisplay(String date)
    {
        String dateMeasured = date;
        Date testDate = null;
        try {
            testDate = sortedFormat.parse(dateMeasured);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return displayFormat.format(testDate);
    }

    //convert date from MM/dd/yyyy to yyyy/MM/dd
    //this makes sorting dates easier
    public static String convertFormatFromDisplayToSorted(String date)
    {
        String dateMeasured = date;
        Date testDate = null;
        try {
            testDate = displayFormat.parse(dateMeasured);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return sortedFormat.format(testDate);
    }

}
