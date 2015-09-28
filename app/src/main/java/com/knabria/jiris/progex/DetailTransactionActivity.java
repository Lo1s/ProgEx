package com.knabria.jiris.progex;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailTransactionActivity extends Activity {

    private static final String TAG = "DetailTransaction";
    private int transactionId;
    private final String URL_TRANSACTION_DETAIL =
            "http://demo0569565.mockable.io/transactions/";
    private ContraAccount contraAccount;
    private Transaction transaction;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Get the passed object (Transaction), update the header data from it and send the request
        // for contra account values
        transaction = getIntent().getParcelableExtra(TransactionsActivity.TRANSACTION);
        transactionId = transaction.getId();
        requestData(URL_TRANSACTION_DETAIL + transactionId);
        updateHeader();
    }

    private void updateHeader() {
        if (transaction != null) {
            ImageView imageViewDirection =
                    (ImageView) findViewById(R.id.imageview_transaction_type_detail);
            TextView textViewAmount = (TextView) findViewById(R.id.textview_amount_detail);
            TextView textViewDirection = (TextView) findViewById(R.id.textview_transfer_type_detail);

            // Set direction icon and text
            if (transaction.getDirection().equals("OUTGOING")) {
                imageViewDirection.setImageResource(R.drawable.ic_arrow_back_black_36dp);
                textViewDirection.setText(R.string.outgoing_transaction);
            } else if (transaction.getDirection().equals("INCOMING")) {
                imageViewDirection.setImageResource(R.drawable.ic_arrow_forward_black_36dp);
                textViewDirection.setText(R.string.incoming_transaction);
            }

            // Set transaction amount (with dummy currency)
            textViewAmount.setText(transaction.getAmountInAccountCurrency()
                    + TransactionsActivity.currency);
        } else {
            Log.d("updateHeader()", "Transaction is Null");
        }
    }

    // Sets up the UI from contra account values
    private void updateDetailView() {
        if (contraAccount != null) {
            TextView textViewAccNumber = (TextView) findViewById(R.id.textview_accountnumber_value);
            TextView textViewAccName = (TextView) findViewById(R.id.textview_accountname_value);
            TextView textViewBankCode = (TextView) findViewById(R.id.textview_bankcode_value);

            progressBar.setVisibility(View.INVISIBLE);
            textViewAccName.setText(contraAccount.getAccountName());
            textViewAccNumber.setText(String.valueOf(contraAccount.getAccountNumber()));
            textViewBankCode.setText(String.valueOf(contraAccount.getBankCode()));
        } else {
            Log.d("updateDetailView()", "ContraAccount is Null");
        }
    }


    private void requestData(String uri) {
        FetchTransacTionDetailTask fetchTransacTionDetailTask
                = new FetchTransacTionDetailTask();
        fetchTransacTionDetailTask.execute(uri);
    }

    private class FetchTransacTionDetailTask extends AsyncTask<String, Void, String> {

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
            Log.i(TAG, "Result: " + s);

            if (s != null) {
                contraAccount = ContraAccountJSONParser.parseFeed(s);
                updateDetailView();

                if (contraAccount != null) {
                    Log.i("accountNumber", contraAccount.getAccountNumber() + "");
                    Log.i("accountName", contraAccount.getAccountName() + "");
                    Log.i("bankCode", contraAccount.getBankCode() + "");
                }

            } else {
                Log.d("onPostExecute()", "Result is Null");
                Toast.makeText(DetailTransactionActivity.this, "Transaction information not found",
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

                if (transaction.getId() == 4) {
                    Toast.makeText(DetailTransactionActivity.this, "Try again ;)",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
