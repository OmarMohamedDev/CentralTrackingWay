package com.omohamed.centraltrackingway.models;

import com.omohamed.centraltrackingway.utils.Utilities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * POJO class that represent an Expense object
 * NOTE: For some fields we use strings instead of the appropriate types (BigDecimal for amount
 * or Date for date, for example) just because Firebase doesn't support this types, but
 * in the app logic this types are used in order to manipulate properly the values.
 * Created by omarmohamed on 30/07/2016.
 */

public class Expense implements Serializable{

    /**
     * Unique identifier for an expense instance
     */
    private String uid;

    /**
     * Amount of the expense
     */
    private String amount;

    /**
     * Description of the expense
     */
    private String description;

    /**
     * Date of the expense (if the expense occurs in several days, it is the first day of the period)
     */
    private String date;

    /**
     * Empty constructor required by firebase platform
     */
    private Expense() {

    }

    /**
     * Private Constructor without parameters called by the factory method
     * and initialized with default values
     */
    private Expense(String uid){
        this.uid = uid;
        this.amount = "0";
        this.description = "Description not available";
        //Today's date
        this.date = Utilities.formatDate(Calendar.getInstance().getTime());
    }

    /**
     * Private Constructor with parameters called by the factory method
     * @param amount amount of the expense
     * @param description description of the expense
     * @param date date of the expense
     */
    private Expense(String uid, String amount, String description, String date){
        this.uid = uid;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    /**
     * Factory method that returns a new expense object with default value
     * @return a new Expense object initialized with default values
     */
    public static Expense generateExpense(String uid){
        return new Expense(uid);
    }

    /**
     * Factory method that returns a new expense object setting the parameters values
     * @param amount amount of the expense
     * @param description description of the expense
     * @param date date of the expense
     * @return a new Expense object initialized with the passed parameters
     */
    public static Expense generateExpense(String uid, BigDecimal amount, String description, Date date){
        String amountString = Utilities.formatAmount(amount);
        String dateString = Utilities.formatDate(date);
        return new Expense(uid, amountString, description, dateString);
    }

    //Getter and Setter
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    //

}
