package com.knabria.jiris.progex;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TransactionsActivity extends Activity {

    private static final String TAG = "TransactionsActivity";
    public static final String TRANSACTION = "transactionObject";
    private final String URL_ALL_TRANSACTIONS = "http://demo0569565.mockable.io/transactions";
    public static String currency = " CZK";

    private int transactionId;

    private ImageView imageView_transactionType;
    private TextView textView_amount;
    private TextView textView_transferType;
    private LinearLayout llContainer;

    private FetchTransactionsTask fetchTransactionsTask;
    private List<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        llContainer = (LinearLayout) findViewById(R.id.linearlayout_container);
        // Initialize the UI components
        // TODO: clean
        // imageView_transactionType = (ImageView) findViewById(R.id.imageview_transactiontype);
        //textView_amount = (TextView) findViewById(R.id.textview_amount);
        //textView_transferType = (TextView) findViewById(R.id.textview_transfertype);

        // Begin the REST transfer in the background thread via AsyncTask
        // for parallel processing use executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params)
        // - not necessary in this example
        requestData(URL_ALL_TRANSACTIONS);
    }

    private synchronized ImageView createImageView(String direction) {
        ImageView imageViewTransacTionType = new ImageView(new ContextThemeWrapper(
                TransactionsActivity.this, R.style.ImageViewStyle));
        imageViewTransacTionType.setId(R.id.imageview_transactiontype);

        if (direction.equals("OUTGOING")) {
            imageViewTransacTionType.setImageResource(R.drawable.ic_arrow_back_black_36dp);
        } else if (direction.equals("INCOMING")) {
            imageViewTransacTionType.setImageResource(R.drawable.ic_arrow_forward_black_36dp);
        } else {
            Toast.makeText(TransactionsActivity.this, "Loading icon failed",
                    Toast.LENGTH_SHORT).show();
        }
        return imageViewTransacTionType;
    }

    private synchronized LinearLayout createTextViewLayout(double amount, String direction) {
        LinearLayout linearLayoutTextViews = new LinearLayout(new ContextThemeWrapper(
                TransactionsActivity.this, R.style.LinearLayoutForTextViewsStyle));

        RelativeLayout.LayoutParams paramsTextViews =
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTextViews.addRule(RelativeLayout.RIGHT_OF, R.id.imageview_transactiontype);
        linearLayoutTextViews.setLayoutParams(paramsTextViews);

        linearLayoutTextViews.addView(createTextView(String.format("%.2f", amount) + currency,
                R.style.TextViewAmountStyle));
        linearLayoutTextViews.addView(createTextView(direction,
                R.style.TextViewTransferTypeStyle));

        return linearLayoutTextViews;
    }

    private synchronized TextView createTextView(String title, int style) {
        TextView textView = new TextView(new ContextThemeWrapper(
                TransactionsActivity.this, style));
        textView.setText(title);

        return textView;
    }

    private synchronized ImageButton createImageButton(int id) {
        ImageButton imageButtonDetail = new ImageButton(new ContextThemeWrapper(
                TransactionsActivity.this, R.style.ImageButtonStyle));
        imageButtonDetail.setImageResource(R.drawable.ic_keyboard_arrow_right_black_36dp);
        RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageButtonDetail.setLayoutParams(imageButtonParams);
        imageButtonDetail.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        final Intent detailActivityIntent = new Intent(TransactionsActivity.this,
                DetailTransactionActivity.class);
        detailActivityIntent.putExtra(TRANSACTION, transactionList.get(id - 1));
        imageButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(detailActivityIntent);
            }
        });

        return imageButtonDetail;
    }

    private synchronized void createCardView(int id, double amount, String direction) {
        CardView cardView = new CardView(new ContextThemeWrapper(TransactionsActivity.this,
                R.style.CardViewStyle), null, 0);
        RelativeLayout cardInner = new RelativeLayout(new ContextThemeWrapper(
                TransactionsActivity.this, R.style.CardViewContentStyle));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = 10;
        params.setMargins(margin, margin, margin, margin);
        cardView.setLayoutParams(params);

        cardInner.addView(createImageView(direction));
        cardInner.addView(createTextViewLayout(amount, direction));
        cardInner.addView(createImageButton(id));

        cardView.addView(cardInner);
        //cardView.setId(cardViewId);
        llContainer.addView(cardView);
    }

    // Checks for internet connectivity
    private boolean isOnline() {
        ConnectivityManager connectMgr =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectMgr.getActiveNetworkInfo();
        if (netInfo != null &&  netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void requestData(String uri) {
        if (isOnline()) {
            fetchTransactionsTask = new FetchTransactionsTask();
            fetchTransactionsTask.execute(uri);
        } else {
            // TODO: Consider making shortcut for enabling Wi-Fi
            Toast.makeText(TransactionsActivity.this, "Device not connected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Fetching list of transactions in JSON from http://demo0569565.mockable.io/transactions
    private class FetchTransactionsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Starting AsyncTask...");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "Fetching data from " + params[0]);
            String jsonString = HttpManager.getDataWithHttpURLConnection(params[0]);
            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "Result: " + s + "\n");
            transactionList = TransactionJSONParser.parseFeed(s);

            if (transactionList != null) {
                for (int i = 0; i < transactionList.size(); i++) {
                    Log.i("id: " + transactionList.get(i).getId(),
                            "Amount: " + transactionList.get(i).getAmountInAccountCurrency()
                            + ", Direction: " + transactionList.get(i).getDirection());
                    createCardView(transactionList.get(i).getId(),
                            transactionList.get(i).getAmountInAccountCurrency(),
                            transactionList.get(i).getDirection());
                }
            } else {
                Log.i(TAG, "Transaction list is null");
            }
        }
    }

    // Starts the activity for detailed transaction information
    // TODO: Change it to fragments for better UI on tablet
    public void startsDetailTransactionActivity(View view) {

    }
}
