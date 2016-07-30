package com.omohamed.centraltrackingway.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class used to gather utility methods
 * Created by omarmohamed on 30/07/2016.
 */

public class Util {

    /**
     * Method that get as input a BigDecimal and return a string formatted
     * in the currency choosed by the user previously
     * @param amount bigdecimal amount that have to be formatted
     * @return string with the value of the bigdecimal
     */
    public static String formatAmount(BigDecimal amount){
        //TODO: Temporary value, retrieve dynamically the correct currency
        Locale currency = Locale.US;
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
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }
}
