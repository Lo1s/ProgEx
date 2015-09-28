package com.knabria.jiris.progex;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DetailTransactionActivity extends Activity {

    private static final String TAG = "DetailTransaction";
    private int transactionId;
    private final String URL_TRANSACTION_DETAIL =
            "http://demo0569565.mockable.io/transactions/";
    private ContraAccount contraAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        transactionId = getIntent().getIntExtra(TransactionsActivity.ID, -1);
        requestData(URL_TRANSACTION_DETAIL + transactionId);
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

                if (contraAccount != null) {
                    Log.i("accountNumber", contraAccount.getAccountNumber() + "");
                    Log.i("accountName", contraAccount.getAccountName() + "");
                    Log.i("bankCode", contraAccount.getBankCode() + "");
                }
            } else {

            }
        }
    }
}
