package com.knabria.jiris.progex;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jiris on 27.09.2015.
 */

// POJO for Transaction object from the JSON
public class Transaction implements Parcelable {

    private int id;
    private double amountInAccountCurrency;
    private String direction;
    private static final int NUMBER_OF_PARAMS = 3;

    public Transaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmountInAccountCurrency() {
        return amountInAccountCurrency;
    }

    public void setAmountInAccountCurrency(double amountInAccountCurrency) {
        this.amountInAccountCurrency = amountInAccountCurrency;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    /** Parcelable constructor & methods to be able
     * to pass this object to detail activity via Intent */

    public Transaction(Parcel in) {
        String[] data = new String[NUMBER_OF_PARAMS];

        in.readStringArray(data);
        setId(Integer.valueOf(data[0]));
        setAmountInAccountCurrency(Double.valueOf(data[1]));
        setDirection(data[2]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                String.valueOf(getId()),
                String.valueOf(getAmountInAccountCurrency()),
                String.valueOf(getDirection())
        });
    }

    public static final Parcelable.Creator<Transaction> CREATOR
            = new Parcelable.Creator<Transaction>() {

        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source); // Using parcelable constructor
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}

