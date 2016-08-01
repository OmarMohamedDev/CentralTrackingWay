package com.omohamed.centraltrackingway.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.models.Expense;
import com.omohamed.centraltrackingway.utils.Constants;
import com.omohamed.centraltrackingway.utils.Utilities;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Fragment used to add, edit or delete an expense
 * @author omarmohamed
 */
public class ManipulateExpenseFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    /**
     * Constant used in the app log
     */
    private static final String TAG = ManipulateExpenseFragment.class.getSimpleName();

    /**
     * Variable used to store the operationType (Add or Edit) passed by as parameter to
     * understand how the fragment should behave
     */
    private String mOperationType;

    /**
     * Expense object passed as parameter that we have to manipulate
     */
    private Expense mExpense;

    /**
     * Description EditText used to manipulate the expense object passed as parameter
     */
    private EditText mDescriptionEditText;

    /**
     * Amount EditText used to manipulate the expense object passed as parameter
     */
    private EditText mAmountEditText;

    /**
     * Date EditText used to manipulate the expense object passed as parameter
     */
    private EditText mDateEditText;

    /**
     * Add button to add a new expense
     */
    private Button mAddButton;

    /**
     * Edit button to edit an expense
     */
    private Button mEditButton;

    /**
     * Delete button to delete an expense
     */
    private Button mDeleteButton;

    public ManipulateExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Factory that creates a new instance of
     * this fragment when the user want to edit/delete it
     *
     * @param operationType Type of operation that should be executed
     * @param expense Expense object that should be manipulated
     * @return A new instance of fragment ManipulateExpenseFragment initialized with the
     *         parameters passed by the users actions
     */
    public static ManipulateExpenseFragment newInstance(String operationType, Expense expense) {
        ManipulateExpenseFragment fragment = new ManipulateExpenseFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CRUDOperations.OPERATION_TYPE, operationType);
        args.putSerializable(Constants.Type.TYPE_EXPENSE, expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().getSerializable(Constants.CRUDOperations.OPERATION_TYPE)
                    .equals(Constants.CRUDOperations.EDIT_EXPENSE)) {
                //Edit Case
                mOperationType = getArguments().getString(Constants.CRUDOperations.OPERATION_TYPE);
                mExpense = (Expense) getArguments().getSerializable(Constants.Type.TYPE_EXPENSE);
            } else {
                //Add Case
                mOperationType = getArguments().getString(Constants.CRUDOperations.OPERATION_TYPE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manipulate_expense, container, false);

        mDescriptionEditText = (EditText) view.findViewById(R.id.edit_text_description);
        mAmountEditText = (EditText) view.findViewById(R.id.edit_text_amount);
        mDateEditText = (EditText) view.findViewById(R.id.edit_text_date);
        mAddButton = (Button) view.findViewById(R.id.btn_add_expense);
        mEditButton = (Button) view.findViewById(R.id.btn_edit_expense);
        mDeleteButton = (Button) view.findViewById(R.id.btn_delete_expense);

        //Checking if we are in the ADD mode or in the EDIT/DELETE MODE
        //and change UI accordingly
        if(mOperationType.equals(Constants.CRUDOperations.ADD_EXPENSE)){
            mEditButton.setVisibility(View.INVISIBLE);
            mDeleteButton.setVisibility(View.INVISIBLE);
            //Setting up the date of today as default for the new expenses
            mDateEditText.setText(Utilities.formatDate(Calendar.getInstance().getTime()));
        } else {
            mAddButton.setVisibility(View.INVISIBLE);

            //Setting up the value already stored inside the object that we have to edit/delete
            mDescriptionEditText.setText(mExpense.getDescription());
            mAmountEditText.setText(mExpense.getAmount().replaceAll(Constants.Patterns.REMOVE_CURRENCIES_SYMBOLS, ""));
            mDateEditText.setText(mExpense.getDate());
        }

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ManipulateExpenseFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        //Listener used by all the buttons
        View.OnClickListener crudButtonsOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Setting up the connection with the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(Constants.DBNodes.USERS);
                String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //

                //Button Delete pressed case
                if(view.getId() == R.id.btn_delete_expense){
                    //Deleting on realtime db
                    myRef.child(userUID)
                            .child(Constants.DBNodes.EXPENSES)
                            .child(mExpense.getUid())
                            .removeValue();

                    //Go back to the previous activity when the operation is ended
                    getActivity().getSupportFragmentManager().popBackStack();

                    //Button Add or Edit pressed
                } else {

                    //If the edit text are filled up properly
                    if(areFieldsFilledCorrectly()) {

                        //Retrieve description
                        String description = mDescriptionEditText.getText().toString();

                        //We transform it in a String, remove the currencies symbols, transform it in BigDecimal
                        BigDecimal amount = new BigDecimal(mAmountEditText.getText().toString()
                                .replaceAll(Constants.Patterns.REMOVE_CURRENCIES_SYMBOLS, ""));

                        //We get the date string, format it following a pattern in utilities and than return the date object
                        Date date = Utilities.fromStringToDate(mDateEditText.getText().toString());

                        //Generating an expense object with the data retrieved by the data
                        //Assigning a new uid if add action, retrieving the old one otherwise
                        if (view.getId() == R.id.btn_add_expense) {
                            mExpense = Expense.generateExpense(myRef.push().getKey(), amount, description, date);

                        } else {
                            mExpense = Expense.generateExpense(mExpense.getUid(), amount, description, date);
                        }
                        //Adding/Editing expense on realtime db
                        myRef.child(userUID)
                                .child(Constants.DBNodes.EXPENSES)
                                .child(mExpense.getUid())
                                .setValue(mExpense);

                        //Go back to the previous activity
                        getActivity().getSupportFragmentManager().popBackStack();

                    } else {
                        //Validation system blocked action and require adjustments
                        Toast.makeText(getActivity(), getString(R.string.fill_all_the_fields), Toast.LENGTH_SHORT).show();
                    }


                }
            }

            /**
             * Check if all the editText are not empty and the date is of the right format
             * @return true if all of the are not empty and the date correct, false otherwise
             */
            private boolean areFieldsFilledCorrectly(){
                //Retrieving strings from UI elements
                String descriptionString = mDescriptionEditText.getText().toString();
                String amountString = mAmountEditText.getText().toString();
                String dateString = mDateEditText.getText().toString();

                //Verifying date validity
                boolean isDateValid = false;

                SimpleDateFormat sdf = new SimpleDateFormat(Constants.Patterns.DATE_FORMAT);
                sdf.setLenient(false);

                try {
                    //if not valid, it will throw ParseException
                    Date date = sdf.parse(dateString);
                    isDateValid = true;

                } catch (ParseException e) {
                    Log.e(ManipulateExpenseFragment.class.getSimpleName(),
                            "Error formatting date: "+ e.getMessage());
                }

                return  !descriptionString.isEmpty()
                        && !amountString.isEmpty()
                        && !dateString.isEmpty()
                        && isDateValid;
            }
        };

        mAddButton.setOnClickListener(crudButtonsOnClickListener);
        mEditButton.setOnClickListener(crudButtonsOnClickListener);
        mDeleteButton.setOnClickListener(crudButtonsOnClickListener);

        return view;
    }

    /**
     * Method called by the DatePicker dialog to set the data picked up by the user
     *
     * @param view        dialog
     * @param year        year selected
     * @param monthOfYear month selected (-1)
     * @param dayOfMonth  day of the month selected
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear + 1) + "/" + year;
        mDateEditText.setText(date);
    }

}


