package com.omohamed.centraltrackingway.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * POJO class that represent an Expense object
 * Created by omarmohamed on 30/07/2016.
 */

public class Expense implements Serializable{
    /**
     * Amount of the expense
     */
    private BigDecimal amount;

    /**
     * Description of the expense
     */
    private String description;

    /**
     * Date of the expense (if the expense occurs in several days, it is the first day of the period)
     */
    private Date date;

    /**
     * Private Constructor without parameters called by the factory method
     * and initialized with default values
     */
    private Expense(){
        this.amount = new BigDecimal("0");
        this.description = "Description not available";
        //Today's date
        this.date = Calendar.getInstance().getTime();
    }

    /**
     * Private Constructor with parameters called by the factory method
     * @param amount amount of the expense
     * @param description description of the expense
     * @param date date of the expense
     */
    private Expense(BigDecimal amount, String description, Date date){
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    /**
     * Factory method that returns a new expense object with default value
     * @return a new Expense object initialized with default values
     */
    public static Expense generateExpense(){
        return new Expense();
    }

    /**
     * Factory method that returns a new expense object setting the parameters values
     * @param amount amount of the expense
     * @param description description of the expense
     * @param date date of the expense
     * @return a new Expense object initialized with the passed parameters
     */
    public static Expense generateExpense(BigDecimal amount, String description, Date date){
        return new Expense(amount, description, date);
    }

    //Getter and Setter
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    //


}
