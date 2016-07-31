package com.omohamed.centraltrackingway.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.models.Expense;
import com.omohamed.centraltrackingway.utils.Constants;
import com.omohamed.centraltrackingway.views.adapters.ExpenseFirebaseAdapter;

import static android.R.anim.fade_in;
import static android.R.anim.fade_out;

//"Transaction between fragments" animations

/**
 * Fragment used to show a list of expense in a RecyclerView
 * @author omarmohamed
 */
public class ExpensesTrackingFragment extends Fragment {

    /**
     * Constant used in the app log
     */
    private static final String TAG = ExpensesTrackingFragment.class.getSimpleName();

    public ExpensesTrackingFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method that create a new instance of
     * this fragment
     *
     * @return A new instance of fragment ExpensesTrackingFragment.
     */
    public static ExpensesTrackingFragment newInstance() {
        ExpensesTrackingFragment fragment = new ExpensesTrackingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expenses_tracking, container, false);

        //Intantiating recycler view to show expenses elements
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.expenses_recycler_view);

        //Retrieve arraylist of expenses from Firebase db
        //Look at instant database
        //Setting up the connection with the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference myRef = database.getReference(Constants.DBNodes.USERS)
                .child(userUID)
                .child(Constants.DBNodes.EXPENSES);

        ExpenseFirebaseAdapter<Expense, ExpenseFirebaseAdapter.ViewHolder> recyclerViewAdapter =
                new ExpenseFirebaseAdapter<Expense, ExpenseFirebaseAdapter.ViewHolder>(Expense.class, R.layout.card_view_expense,
                                                ExpenseFirebaseAdapter.ViewHolder.class,  myRef) {
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
        //

        //Setting up recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        //

        //Setting up Floating Action Button to permit to the user to add new expenses
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
        //

        return view;
    }
}
