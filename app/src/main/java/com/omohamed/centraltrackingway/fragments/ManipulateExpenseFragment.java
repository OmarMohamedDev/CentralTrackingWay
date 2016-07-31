package com.omohamed.centraltrackingway.fragments;

import android.content.Context;
import android.net.Uri;
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
 */
public class ManipulateExpenseFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private String mOperationType;
    private Expense mExpense;
    private EditText mDescription;
    private EditText mAmount;
    private EditText mDate;
    private Button mAddButton;
    private Button mEditButton;
    private Button mDeleteButton;

    private OnFragmentInteractionListener mListener;

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
            //Edit Case
            if(getArguments().getSerializable(Constants.CRUDOperations.OPERATION_TYPE)
                    .equals(Constants.CRUDOperations.EDIT_EXPENSE)) {
                mOperationType = getArguments().getString(Constants.CRUDOperations.OPERATION_TYPE);
                mExpense = (Expense) getArguments().getSerializable(Constants.Type.TYPE_EXPENSE);
            } else { //Add Case
                mOperationType = getArguments().getString(Constants.CRUDOperations.OPERATION_TYPE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manipulate_expense, container, false);

        mDescription = (EditText) view.findViewById(R.id.edit_text_description);
        mAmount = (EditText) view.findViewById(R.id.edit_text_amount);
        mDate = (EditText) view.findViewById(R.id.edit_text_date);
        mAddButton = (Button) view.findViewById(R.id.btn_add_expense);
        mEditButton = (Button) view.findViewById(R.id.btn_edit_expense);
        mDeleteButton = (Button) view.findViewById(R.id.btn_delete_expense);

        //Checking if we are in the ADD mode or in the EDIT/DELETE MODE
        if(mOperationType.equals(Constants.CRUDOperations.ADD_EXPENSE)){
            mEditButton.setVisibility(View.INVISIBLE);
            mDeleteButton.setVisibility(View.INVISIBLE);
            //Setting up the date of today as default for the new expenses
            mDate.setText(Utilities.formatDate(Calendar.getInstance().getTime()));
        } else {
            mAddButton.setVisibility(View.INVISIBLE);

            //Setting up the value already stored inside the object that we have to edit/delete
            mDescription.setText(mExpense.getDescription());
            mAmount.setText(mExpense.getAmount().replaceAll(Constants.Patterns.REMOVE_CURRENCIES_SYMBOLS,""));
            mDate.setText(mExpense.getDate());
        }

        //TODO: Fix the problem that we have to click twice on the edit text in order to call the picker dialog
        mDate.setOnClickListener(new View.OnClickListener() {
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

                if(view.getId() == R.id.btn_delete_expense){
                    myRef.child(userUID)
                            .child(Constants.DBNodes.EXPENSES)
                            .child(mExpense.getUid())
                            .removeValue();

                    //Go back to the previous activity
                    getActivity().getSupportFragmentManager().popBackStack();

                } else {

                    if(areFieldsFilledCorrectly()) {

                        String description = mDescription.getText().toString();

                        //We transform it in a String, remove the currencies symbols, transform it in BigDecimal
                        BigDecimal amount = new BigDecimal(mAmount.getText().toString()
                                .replaceAll(Constants.Patterns.REMOVE_CURRENCIES_SYMBOLS, ""));

                        //We get the date string, format it following a pattern in utilities and than return the date object
                        Date date = Utilities.fromStringToDate(mDate.getText().toString());

                        if (view.getId() == R.id.btn_add_expense) {
                            mExpense = Expense.generateExpense(myRef.push().getKey(), amount, description, date);

                        } else {
                            mExpense = Expense.generateExpense(mExpense.getUid(), amount, description, date);
                        }

                        myRef.child(userUID)
                                .child(Constants.DBNodes.EXPENSES)
                                .child(mExpense.getUid())
                                .setValue(mExpense);

                        //Go back to the previous activity
                        getActivity().getSupportFragmentManager().popBackStack();

                    } else {
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
                String descriptionString = mDescription.getText().toString();
                String amountString = mAmount.getText().toString();
                String dateString =  mDate.getText().toString();

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

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        mDate.setText(date);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}


