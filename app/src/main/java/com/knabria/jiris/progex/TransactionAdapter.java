package com.knabria.jiris.progex;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jiris on 29.09.2015.
 */
public class TransactionAdapter extends RecyclerView.Adapter<
        TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> mTransactionList;
    private static final String TAG = "TransactionAdapter";

    public TransactionAdapter(List<Transaction> dataSet) {
        mTransactionList = dataSet;
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageViewDirection;
        protected LinearLayout linearLayoutTextViews;
        protected TextView textViewAmount;
        protected TextView textViewDirection;
        protected ImageButton imageButtonDetail;

        public TransactionViewHolder(View v) {
            super(v);
            imageViewDirection = (ImageView) v.findViewById(R.id.imageview_transaction_direction);
            linearLayoutTextViews =
                    (LinearLayout) v.findViewById(R.id.linearlayout_transaction_info);
            textViewAmount = (TextView) v.findViewById(R.id.textview_amount);
            textViewDirection = (TextView) v.findViewById(R.id.textview_direction);
            imageButtonDetail = (ImageButton) v.findViewById(R.id.imagebutton_detail);
        }
    }

    @Override
    public void onBindViewHolder(final TransactionViewHolder holder, int position) {
        // Replace contents of the view with element on this position
        Transaction transaction = mTransactionList.get(position);

        if (transaction.getDirection().equals(MyConstants.FILTER_INCOMING)) {
            holder.imageViewDirection.setImageResource(R.drawable.ic_arrow_forward_black_36dp);
            holder.textViewDirection.setText(R.string.incoming_transaction);
        } else if (transaction.getDirection().equals(MyConstants.FILTER_OUTGOING)) {
            holder.imageViewDirection.setImageResource(R.drawable.ic_arrow_back_black_36dp);
            holder.textViewDirection.setText(R.string.outgoing_transaction);
        } else {
            Log.e(TAG, "Loading icon failed");
        }
        holder.textViewAmount.setText(transaction.getAmountInAccountCurrency()
                + MyConstants.currency);
        /*holder.imageButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://stackoverflow.com/questions/28767413/how-to-open-a-different-activity-on-recyclerview-item-onclick
            }
        });*/
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // New View created here
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardview, parent, false);

        // Layout properties can be set here
        //...

        // TODO: Check if casting is ok
        TransactionViewHolder vh = new TransactionViewHolder((CardView)view);
        return vh;
    }


    // Returns size of the dataset
    @Override
    public int getItemCount() {
        return mTransactionList.size();
    }
}
