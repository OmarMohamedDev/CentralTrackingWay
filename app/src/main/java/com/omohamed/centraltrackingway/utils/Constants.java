package com.omohamed.centraltrackingway.utils;

/**
 * Class used to store constants used in the application
 * Created by omarmohamed on 30/07/2016.
 */

public class Constants {

    /**
     * Constants used to indicate which type of operation we need to execute
     */
    public class CRUDOperations{
        public static final String ADD_EXPENSE = "ADD_EXPENSE";
        public static final String EDIT_EXPENSE = "EDIT_EXPENSE";
        public static final String DELETE_EXPENSE = "DELETE_EXPENSE";
    }

    /**
     * Constants used to indicate a type of data
     */
    public class Type{
        public static final String TYPE_EXPENSE = "EXPENSE";
    }

    /**
     * Constants used to define date patterns, regular expression, etc.
     */
    public class Patterns{
        public static final String DATE_FORMAT = "dd-MM-yyyy";
        public static final String REMOVE_CURRENCIES_SYMBOLS = "[^0-9.,]+";
    }

}
