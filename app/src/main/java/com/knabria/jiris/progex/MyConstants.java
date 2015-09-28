package com.knabria.jiris.progex;

/**
 * Created by jiris on 28.09.2015.
 */

// Making class final to prevent from subclassing
public final class MyConstants {

    private MyConstants() {
        // Making constructor private to prevent from instantiating
    }

    public static final String TRANSACTION_OBJECT = "transactionObject";
    public static String currency = " CZK";
    public static final String FILTER_INCOMING = "INCOMING";
    public static final String FILTER_OUTGOING = "OUTGOING";
    public static final String FILTER_ALL = "ALL";

}
