package com.knabria.jiris.progex;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiris on 27.09.2015.
 */
public class TransactionJSONParser {

    // Parses data from JSON to the list of transactions
    public static List<Transaction> parseFeed(String content, String filter, Context context) {

        try {
            JSONObject jsonObject = new JSONObject(content);
            List<Transaction> transactionList = new ArrayList<>();

            JSONArray jsonArray = jsonObject.getJSONArray("items");

            // In case more objects would exist
            for (int i = 0; i < jsonObject.length(); i++) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    Transaction transaction = new Transaction();

                    transaction.setId(jsonArray.getJSONObject(j)
                            .getInt("id"));
                    transaction.setAmountInAccountCurrency(jsonArray.getJSONObject(j)
                            .getInt("amountInAccountCurrency"));
                    transaction.setDirection(jsonArray.getJSONObject(j)
                            .getString("direction"));

                    // Adding only necessary transactions (given by the filter = Tab)
                    if (filter.equals(context.getResources().getString(R.string.all_transactions))) {
                        transactionList.add(transaction);
                    } else if (filter.equals(context.getResources().getString(
                            R.string.incoming_transaction)) && transaction.getDirection().equals(
                            MyConstants.FILTER_INCOMING)) {
                        transactionList.add(transaction);
                    } else if (filter.equals(context.getResources().getString(
                            R.string.outgoing_transaction)) && transaction.getDirection().equals(
                            MyConstants.FILTER_OUTGOING)) {
                        transactionList.add(transaction);
                    }
                }
            }

            return transactionList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Returns list with parsed JSON data
    public static List<Transaction> parseFeed(JSONObject jsObject, String filter, Context context) {

        try {
            JSONObject jsonObject = jsObject;
            List<Transaction> transactionList = new ArrayList<>();

            JSONArray jsonArray = jsonObject.getJSONArray("items");

            // In case more objects would exist
            for (int i = 0; i < jsonObject.length(); i++) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    Transaction transaction = new Transaction();

                    transaction.setId(jsonArray.getJSONObject(j)
                            .getInt("id"));
                    transaction.setAmountInAccountCurrency(jsonArray.getJSONObject(j)
                            .getInt("amountInAccountCurrency"));
                    transaction.setDirection(jsonArray.getJSONObject(j)
                            .getString("direction"));

                    // Adding only necessary transactions (given by a filter = Tab)
                    if (filter.equals(context.getResources().getString(R.string.all_transactions))) {
                        transactionList.add(transaction);
                    } else if (filter.equals(context.getResources().getString(
                            R.string.incoming_transaction)) && transaction.getDirection().equals(
                            MyConstants.FILTER_INCOMING)) {
                        transactionList.add(transaction);
                    } else if (filter.equals(context.getResources().getString(
                            R.string.outgoing_transaction)) && transaction.getDirection().equals(
                            MyConstants.FILTER_OUTGOING)) {
                        transactionList.add(transaction);
                    }
                }
            }

            return transactionList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
