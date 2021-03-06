package com.omohamed.centraltrackingway.utils;

/**
 * Class used to store constants used in the application
 * @author omarmohamed t
 */

public class Constants {

    private Constants(){
        //Private constructor for non-instantiable class
    }

    /**
     * Constants used to indicate which type of operation we need to execute
     */
    public static class CRUDOperations{
        public static final String OPERATION_TYPE = "OPERATION_TYPE";
        public static final String ADD_EXPENSE = "ADD_EXPENSE";
        public static final String EDIT_EXPENSE = "EDIT_EXPENSE";
        public static final String DELETE_EXPENSE = "DELETE_EXPENSE";
    }

    /**
     * Constants used to indicate a type of data
     */
    public static class Type{
        public static final String TYPE_EXPENSE = "EXPENSE";
        public static final String TYPE_UID = "UID";
    }

    /**
     * Constants used to define date patterns, regular expression, etc.
     */
    public static class Patterns{
        public static final String DATE_FORMAT = "d/M/yyyy";
        public static final String REMOVE_CURRENCIES_SYMBOLS = "[^0-9.]+";
    }

    /**
     * Constants used to identify the nodes of the NoSQL db
     */
    public class DBNodes{
        public static final String USERS = "users";
        public static final String EXPENSES = "expenses";
        public static final String CREATION_DATE = "creationDate";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

}
