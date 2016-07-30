package com.omohamed.centraltrackingway.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omohamed.centraltrackingway.R;
import com.omohamed.centraltrackingway.models.Expense;
import com.omohamed.centraltrackingway.utils.Util;

import java.util.ArrayList;

/**
 * Adapter used to represent the expenses data in the recycler view
 * Created by omarmohamed on 30/07/2016.
 */

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {
    private ArrayList<Expense> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mCardView;
        public TextView mExpenseDescription;
        public TextView mExpenseAmount;
        public TextView mExpenseDate;
        public ViewHolder(View v) {
            super(v);
            mCardView = v;
            mExpenseDescription = (TextView) v.findViewById(R.id.expense_description_text_view);
            mExpenseAmount = (TextView) v.findViewById(R.id.expense_amount_text_view);
            mExpenseDate = (TextView) v.findViewById(R.id.expense_date_text_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExpensesAdapter(ArrayList<Expense> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExpensesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_expense, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String description = mDataset.get(position).getDescription();
        String amount = Util.formatAmount(mDataset.get(position).getAmount());
        String date = Util.formatDate(mDataset.get(position).getDate());

        holder.mExpenseDescription.setText(description);
        holder.mExpenseAmount.setText(amount);
        holder.mExpenseDate.setText(date);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}