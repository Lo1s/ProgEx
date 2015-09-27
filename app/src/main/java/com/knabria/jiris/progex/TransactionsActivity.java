package com.knabria.jiris.progex;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionsActivity extends Activity {

    private final String TAG = "TransactionsActivity";
    private final String URI = "http://demo0569565.mockable.io/transactions";

    private ImageView imageView_transactionType;
    private TextView textView_amount;
    private TextView textView_transferType;

    private FetchTransactionsTask fetchTransactionsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        // Initialize the UI components
        imageView_transactionType = (ImageView) findViewById(R.id.imageview_transactiontype);
        textView_amount = (TextView) findViewById(R.id.textview_amount);
        textView_transferType = (TextView) findViewById(R.id.textview_transfertype);

        // Begin the REST transfer in the background thread via AsyncTask
        // for parallel processing use executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params)
        // - not necessary in this example
        requestData(URI);
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
            Log.i(TAG, "Fetching data...");

            String jsonString = HttpManager.getDataWithHttpURLConnection(params[0]);

            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "Result Complete:\n" + s);
        }
    }

    // Starts the activity for detailed transaction information
    // TODO: Change it to fragments for better UI on tablet
    public void startsDetailTransactionActivity(View view) {
        startActivity(new Intent(this, DetailTransactionActivity.class));
    }
}
