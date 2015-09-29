package com.knabria.jiris.progex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private static final String TAG = "TransactionsActivity";
    private final String URL_ALL_TRANSACTIONS = "http://demo0569565.mockable.io/transactions";
    private String selectedTab = MyConstants.FILTER_ALL;

    private ActionBar actionBar;
    private ProgressBar progressBar;

    private FetchTransactionsTask fetchTransactionsTask;
    private List<Transaction> transactionList;

    // RecyclerView for better performance in case great amount of data would be loaded
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private NetworkReceiver receiver;
    private IntentFilter intentFilter;

    // Notify if the user turned wifi on when the state was off during app runtime
    // This way transaction list automatically refreshes if user selected "Yes" in the dialog
    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (isOnline()) {
                    requestData(URL_ALL_TRANSACTIONS);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        progressBar = (ProgressBar) findViewById(R.id.progressBarTransactions);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // TODO: Check the layout on different screen sizes
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        actionBar = getSupportActionBar();
        initializeTabs();

        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        receiver = new NetworkReceiver();
        registerReceiver(receiver, intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    private void initializeTabs() {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getResources().getString(R.string.transaction_list));

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                progressBar.setVisibility(View.VISIBLE);
                selectedTab = tab.getText().toString();
                // Begin the REST transfer in the background thread via AsyncTask
                // for parallel processing use executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params)
                // - not necessary in this example
                requestData(URL_ALL_TRANSACTIONS);

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };

        actionBar.addTab(actionBar.newTab().setText(R.string.all_transactions)
                .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(R.string.incoming_transaction)
                .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText(R.string.outgoing_transaction)
                .setTabListener(tabListener));
    }

    // Checks for internet connectivity
    private boolean isOnline() {
        ConnectivityManager connectMgr =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
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
            askUserToTurnWifiOn();
        }
    }

    private void askUserToTurnWifiOn() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);

        dialogBuilder.setTitle("Wi-Fi Settings");

        dialogBuilder
                .setMessage("Enable Wi-Fi?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wifiMgr.setWifiEnabled(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wifiMgr.setWifiEnabled(false);
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
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
            transactionList = TransactionJSONParser.parseFeed(s, selectedTab,
                    TransactionsActivity.this);

            if (transactionList != null) {
                mAdapter = new TransactionAdapter(transactionList);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                Log.i(TAG, "Transaction list is null");
            }
        }
    }

    // TODO: Change it to fragments for better UI on tablet

}
