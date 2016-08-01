package com.omohamed.centraltrackingway.utils;

import android.util.Log;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Unit tests for Utilities class
 * Created by omarmohamed on 1/08/2016.
 */
public class UtilitiesTest {

    @Test
    public void formatAmountNotNullValue() throws Exception {
        Locale currency = Locale.getDefault();
        NumberFormat costFormat = NumberFormat.getCurrencyInstance(currency);
        costFormat.setMinimumFractionDigits(1);
        costFormat.setMaximumFractionDigits(2);
        String stringExpected = costFormat.format(new BigDecimal("0"));

        assertEquals(stringExpected, Utilities.formatAmount(new BigDecimal("0")));
    }

    @Test
    public void formatDateNullDate() throws Exception {
        assertNull(Utilities.formatDate(null));
    }

    @Test
    public void formatDateNotNullDate() throws Exception {
        String expectedDate = new SimpleDateFormat(Constants.Patterns.DATE_FORMAT)
                .format(Calendar.getInstance().getTime());

        assertEquals(expectedDate, Utilities.formatDate(Calendar.getInstance().getTime()));
    }

    @Test
    public void fromStringToDateEmptyString() throws Exception {
        assertNull(Utilities.fromStringToDate(""));
    }

    @Test
    public void fromStringToDateCorrectDate() throws Exception {
        String dateString = Utilities.formatDate(Calendar.getInstance().getTime());

        Date dateExpected = null;

        try {
            dateExpected = new SimpleDateFormat(Constants.Patterns.DATE_FORMAT).parse(dateString);
        } catch (ParseException pe) {
            Log.e(Utilities.class.getSimpleName(), "Parse Exeption trying to convert the date from string to date format" + pe);
        }

        assertEquals(dateExpected,
                Utilities.fromStringToDate(Utilities.formatDate(Calendar.getInstance().getTime())));
    }

}