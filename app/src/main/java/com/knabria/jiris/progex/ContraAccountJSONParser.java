package com.knabria.jiris.progex;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiris on 27.09.2015.
 */
public class ContraAccountJSONParser {

    public static ContraAccount parseFeed(String content) {

        try {
            JSONObject jsonObject = new JSONObject(content);
            ContraAccount contraAccount = new ContraAccount();

            contraAccount.setAccountNumber(jsonObject.getJSONObject("contraAccount")
                    .getInt("accountNumber"));
            contraAccount.setAccountName(jsonObject.getJSONObject("contraAccount")
                    .getString("accountName"));
            contraAccount.setBankCode(jsonObject.getJSONObject("contraAccount")
                    .getInt("bankCode"));


            return contraAccount;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ContraAccount parseFeed(JSONObject jsobject) {

        try {
            JSONObject jsonObject = jsobject;
            ContraAccount contraAccount = new ContraAccount();

            contraAccount.setAccountNumber(jsonObject.getJSONObject("contraAccount")
                    .getInt("accountNumber"));
            contraAccount.setAccountName(jsonObject.getJSONObject("contraAccount")
                    .getString("accountName"));
            contraAccount.setBankCode(jsonObject.getJSONObject("contraAccount")
                    .getInt("bankCode"));


            return contraAccount;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
