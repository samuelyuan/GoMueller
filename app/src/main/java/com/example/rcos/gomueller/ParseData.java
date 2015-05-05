package com.example.rcos.gomueller;

import java.text.SimpleDateFormat;

public class ParseData
{
    public static String getAttributeValue(String currentDetailStr)
    {
        String[] splitString = currentDetailStr.split(" ");
        for (int i = 0; i < splitString.length - 1; i++)
        {
            if (splitString[i].equals("Weight:"))
                return String.valueOf(splitString[i + 1]);
        }

        return "";
    }

    public static String getDate(String currentDetailStr)
    {
        //parse the string
        String[] splitString = currentDetailStr.split(" ");
        String dateStr = splitString[0];

        //convert date from yyyy/MM/dd to MM/dd/yyyy
        //then parse that date
        //this is for displaying data on graph
        return UnitDate.convertFormatFromSortedToDisplay(dateStr);
    }

    public static String getNotes(String currentDetailStr) {
        return currentDetailStr.substring(currentDetailStr.indexOf("Notes: ") + ("Notes: ").length());
    }

    public static String getDetailString(String dateMeasured, String weightStr, String whichLabel, String notes)
    {
        //database stores dates in MM/dd/yy format,
        // but sorting is in yyyy/MM/dd
        String dateInSortedFormat = UnitDate.convertFormatFromDisplayToSorted(dateMeasured);

        //weight is assumed to be in proper units with the correct label
        return dateInSortedFormat + " : Weight: " + weightStr + " " + whichLabel + " Notes: " + notes;
    }

}
