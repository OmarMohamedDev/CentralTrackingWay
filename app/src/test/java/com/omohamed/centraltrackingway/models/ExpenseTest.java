package com.omohamed.centraltrackingway.models;

import com.omohamed.centraltrackingway.utils.Utilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Unit tests for Expense class
 * Created by omarmohamed on 31/07/2016.
 */
public class ExpenseTest {

    //Expense object that we will use to make the tests
    Expense mExpense;

    //Fake uid that we will use to test the expense object
    String mFakeUID;

    @Before
    public void setUp() throws Exception {
        mFakeUID = "12345";
    }

    @After
    public void tearDown() throws Exception {
        //Cleaning up the objects
        mFakeUID = "";
        mExpense = null;
    }

    @Test
    public void generateExpenseWithParameters() throws Exception {
        //Creating the new instance with parameters
        mExpense = Expense.generateExpense(mFakeUID, new BigDecimal("35125"), "New Car", Calendar.getInstance().getTime());

        //Checking is not null
        assertNotNull(mExpense);

        //Checking that the data passed by parameter is correctly stored in the object
        assertEquals(mFakeUID, mExpense.getUid());
        assertEquals(Utilities.formatAmount(new BigDecimal("35125")), mExpense.getAmount());
        assertEquals(Utilities.formatDate(Calendar.getInstance().getTime()), mExpense.getDate());
        assertEquals("New Car", mExpense.getDescription());

    }

    @Test
    public void getDateWhenEmptyGenerateInstanceIsUsed() throws Exception {
        //Creating the new instance
        mExpense = Expense.generateExpense(mFakeUID);

        //Checking is not null
        assertNotNull(mExpense);

        //Checking that the data are the one arbitrarily determined in the constructor with
        //just the uid as parameter (Date of today, uid passed by, 0 as amount and "Description not available"
        //as description
        assertEquals(mFakeUID, mExpense.getUid());
        assertEquals("0", mExpense.getAmount());
        assertEquals(Utilities.formatDate(Calendar.getInstance().getTime()), mExpense.getDate());
        assertEquals("Description not available", mExpense.getDescription());
    }

}