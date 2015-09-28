package com.knabria.jiris.progex;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiris on 27.09.2015.
 */
public class TransactionJSONParser {

    // Returns list with parsed JSON data
    public static List<Transaction> parseFeed(String content) {

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

                    transactionList.add(transaction);
                }
            }

            return transactionList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
