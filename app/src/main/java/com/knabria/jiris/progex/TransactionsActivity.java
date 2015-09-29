package com.knabria.jiris.progex;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private static final String TAG = "TransactionsActivity";
    private final String URL_ALL_TRANSACTIONS = "http://demo0569565.mockable.io/transactions";
    private String selectedTab = MyConstants.FILTER_ALL;

    //private LinearLayout llContainer;
    private ActionBar actionBar;

    private FetchTransactionsTask fetchTransactionsTask;
    private List<Transaction> transactionList;
    private Transaction transaction;

    // RecyclerView for better performance in case great amount of data would be loaded
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        //llContainer = (LinearLayout) findViewById(R.id.linearlayout_container);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // TODO: Check the layout on different screen sizes
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        actionBar = getSupportActionBar();
        initializeTabs();

    }

    private void initializeTabs() {
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getResources().getString(R.string.transaction_list));

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                /*if (llContainer.getChildCount() > 0) {
                    llContainer.removeAllViews();
                }*/

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

    /** Helper methods for creating UI which consists from CardViews */
    /*private synchronized void createCardView(Transaction transaction) {
        // For localization purposes get string from resources
        String directionFromResources =
                (transaction.getDirection().equals(MyConstants.FILTER_INCOMING)) ?
                        getResources().getString(R.string.incoming_transaction) :
                        getResources().getString(R.string.outgoing_transaction);

        CardView cardView = new CardView(new ContextThemeWrapper(TransactionsActivity.this,
                R.style.CardViewStyle), null, 0);

        RelativeLayout cardInner = new RelativeLayout(new ContextThemeWrapper(
                TransactionsActivity.this, R.style.CardViewContentStyle));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int margin = 10;
        params.setMargins(margin, margin, margin, 10);
        cardView.setLayoutParams(params);

        cardInner.addView(createImageView(transaction.getDirection()));
        cardInner.addView(createTextViewLayout(transaction.getAmountInAccountCurrency(),
                directionFromResources));
        cardInner.addView(createImageButton(transaction.getId()));

        cardView.addView(cardInner);
        llContainer.addView(cardView);
    }

    private synchronized ImageView createImageView(String direction) {
        ImageView imageViewTransacTionType = new ImageView(new ContextThemeWrapper(
                TransactionsActivity.this, R.style.ImageViewStyle));
        imageViewTransacTionType.setId(R.id.imageview_transactiontype);

        if (direction.equals(MyConstants.FILTER_OUTGOING)) {
            imageViewTransacTionType.setImageResource(R.drawable.ic_arrow_back_black_36dp);
        } else if (direction.equals(MyConstants.FILTER_INCOMING)) {
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

        linearLayoutTextViews.addView(createTextView(String.format("%.2f", amount)
                + MyConstants.currency, R.style.TextViewAmountStyle));
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
        detailActivityIntent.putExtra(MyConstants.TRANSACTION_OBJECT, transactionList.get(id - 1));

        // Register listener
        imageButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(detailActivityIntent);
            }
        });

        return imageButtonDetail;
    }*/

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
            transactionList = TransactionJSONParser.parseFeed(s);

            if (transactionList != null) {
                mAdapter = new TransactionAdapter(transactionList);
                mRecyclerView.setAdapter(mAdapter);
                /*for (int i = 0; i < transactionList.size(); i++) {
                    transaction = transactionList.get(i);
                    if (selectedTab.equals(getResources().getString(R.string.all_transactions))) {
                        Log.i("id: " + transaction.getId(),
                                "Amount: " + transaction.getAmountInAccountCurrency()
                                        + ", Direction: " + transaction.getDirection());
                            //createCardView(transaction);

                    } else if (selectedTab.equals(getResources()
                            .getString(R.string.incoming_transaction))
                            && transaction.getDirection().equals(MyConstants.FILTER_INCOMING)) {
                        //createCardView(transaction);

                    } else if (selectedTab.equals(getResources()
                            .getString(R.string.outgoing_transaction))
                            && transaction.getDirection().equals(MyConstants.FILTER_OUTGOING)) {
                        //createCardView(transaction);
                    }
                }*/
            } else {
                Log.i(TAG, "Transaction list is null");
            }
        }
    }

    // TODO: Change it to fragments for better UI on tablet

}
