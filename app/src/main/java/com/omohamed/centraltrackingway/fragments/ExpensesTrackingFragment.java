package com.omohamed.centraltrackingway.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.activities.AuthActivity;
import com.omohamed.centraltrackingway.models.Expense;
import com.omohamed.centraltrackingway.utils.Constants;
import com.omohamed.centraltrackingway.views.adapters.ExpenseFirebaseAdapter;

import java.util.ArrayList;

import static android.R.anim.fade_in;
import static android.R.anim.fade_out;

/**
 *
 * Fragment used to show a list of expense in a RecyclerView
 */
public class ExpensesTrackingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Expense> mExpensesList;

    private OnFragmentInteractionListener mListener;

    public ExpensesTrackingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpensesTrackingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpensesTrackingFragment newInstance(String param1, String param2) {
        ExpensesTrackingFragment fragment = new ExpensesTrackingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expenses_tracking, container, false);

        //Setting up the adapter in order to show up the data retrieved by the server in the
        //recycler view
//        ExpensesAdapter adapter = new ExpensesAdapter(mExpensesList);
          RecyclerView recyclerView =  (RecyclerView)view.findViewById(R.id.expenses_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
//        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(llm);
        //

        //Retrieve arraylist of expenses from firebase
        //Look at instant database
        //Setting up the connection with the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
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

        DatabaseReference myRef = database.getReference(Constants.DBNodes.USERS)
                .child(userUID)
                .child(Constants.DBNodes.EXPENSES);
//                .child("2151b2eb-c2a0-4a40-81ff-4878dfe9a423");

        ExpenseFirebaseAdapter<Expense, ExpenseFirebaseAdapter.ViewHolder> recyclerViewAdapter = new ExpenseFirebaseAdapter<Expense, ExpenseFirebaseAdapter.ViewHolder>(Expense.class, R.layout.card_view_expense, ExpenseFirebaseAdapter.ViewHolder.class,  myRef) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Expense model, int position) {
                String uid = model.getUid();
                String description = model.getDescription();
                String amount = model.getAmount();
                String date = model.getDate();

                viewHolder.mExpenseUID = uid;
                viewHolder.mExpenseDescription.setText(description);
                viewHolder.mExpenseAmount.setText(amount);
                viewHolder.mExpenseDate.setText(date);
            }
        } ;
       // recyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
       // recyclerViewAdapter.notifyDataSetChanged();


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(fade_in, fade_out)
                        .addToBackStack(null)
                        .replace(R.id.core_fragment_container,
                                 ManipulateExpenseFragment
                                         .newInstance(Constants.CRUDOperations.ADD_EXPENSE, null))
                        .commit();
            }
        });

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
