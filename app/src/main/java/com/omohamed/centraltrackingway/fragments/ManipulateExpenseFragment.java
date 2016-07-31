package com.omohamed.centraltrackingway.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.activities.AuthActivity;
import com.omohamed.centraltrackingway.models.Expense;
import com.omohamed.centraltrackingway.utils.Constants;
import com.omohamed.centraltrackingway.utils.Utilities;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
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
    private EditText mDateField;
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
        args.putString(Constants.CRUDOperations.EDIT_EXPENSE, operationType);
        args.putSerializable(Constants.Type.TYPE_EXPENSE, expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Edit Case
            if(getArguments().getSerializable(Constants.Type.TYPE_EXPENSE) != null) {
                mOperationType = getArguments().getString(Constants.CRUDOperations.EDIT_EXPENSE);
                mExpense = (Expense) getArguments().getSerializable(Constants.Type.TYPE_EXPENSE);
            } else { //Add Case
                mOperationType = getArguments().getString(Constants.CRUDOperations.ADD_EXPENSE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manipulate_expense, container, false);

        mDescription = (EditText) view.findViewById(R.id.edit_text_description);
        mAmount = (EditText) view.findViewById(R.id.edit_text_amount);
        mDateField = (EditText) view.findViewById(R.id.edit_text_date);
        mAddButton = (Button) view.findViewById(R.id.btn_add_expense);
        mEditButton = (Button) view.findViewById(R.id.btn_edit_expense);
        mDeleteButton = (Button) view.findViewById(R.id.btn_delete_expense);

        //TODO: Fix the problem that we have to click twice on the edit text in order to call the picker dialog
        mDateField.setOnClickListener(new View.OnClickListener() {
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
                String userUID = "";

                //Check on the user: if slogged, request auth, get the email otherwise
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name and email
                    userUID= user.getUid();
                } else {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getActivity(), AuthActivity.class));
                    getActivity().finish();
                }

                if(view.getId() == R.id.btn_delete_expense){
                    myRef.child(userUID)
                            .child(Constants.DBNodes.EXPENSES)
                            .child(mExpense.getUid())
                            .removeValue();

                } else {

                    String description = mDescription.getText().toString();

                    //We transform it in a String, remove the currencies symbols, transform it in BigDecimal
                    BigDecimal amount = new BigDecimal(mAmount.getText().toString()
                            .replaceAll(Constants.Patterns.REMOVE_CURRENCIES_SYMBOLS, ""));

                    //We get the date string, format it following a pattern in utilities and than return the date object
                    Date date = Utilities.fromStringToDate(mDateField.getText().toString());

                    if(view.getId() == R.id.btn_add_expense) {
                        mExpense = Expense.generateExpense(myRef.push().getKey(), amount, description, date);

                    } else {
                        mExpense = Expense.generateExpense(mExpense.getUid(), amount, description, date);
                    }

                    myRef.child(userUID)
                            .child(Constants.DBNodes.EXPENSES)
                            .child(mExpense.getUid())
                            .setValue(mExpense);


                }

                //Go back to the previous activity
                getActivity().getSupportFragmentManager().popBackStack();
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
        mDateField.setText(date);
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


