package com.omohamed.centraltrackingway.utils;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class used to gather utility methods
 * @author omarmohamed
 */

public class Utilities {

    /**
     * Method that get as input a BigDecimal and return a string formatted
     * in the local currency of the user
     * @param amount BigDecimal amount that have to be formatted
     * @return string with the value of the bigdecimal
     */
    public static String formatAmount(BigDecimal amount){
        Locale currency = Locale.getDefault();
        NumberFormat costFormat = NumberFormat.getCurrencyInstance(currency);
        costFormat.setMinimumFractionDigits( 1 );
        costFormat.setMaximumFractionDigits( 2 );
        return costFormat.format(amount.doubleValue());
    }

    /**
     * Method that get as input a Date and return a string formatted representing the date
     * @param date Date value
     * @return string with the representation of the date
     */
    public static String formatDate(Date date){
        return new SimpleDateFormat(Constants.Patterns.DATE_FORMAT).format(date);
    }

    /**
     * Method that get as input a String and return a date object
     * @param dateString date as String
     * @return date object that represent the one passed as string
     */
    public static Date fromStringToDate(String dateString){
        Date date = null;

        try{
            date = new SimpleDateFormat(Constants.Patterns.DATE_FORMAT).parse(dateString);
        } catch (ParseException pe){
            Log.e(Utilities.class.getSimpleName(),"Parse Exeption trying to convert the date from string to date format"+pe);
        }
        return date;
    }

    /**
     * Method used in order to scale a BigDecimal always using the same rule inside the app
     * @param bigDecimal that have to be rounded
     * @return rounded bigdecimal
     */
    public static BigDecimal roundUpBigDecimal(BigDecimal bigDecimal){
        return bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
    }
}
