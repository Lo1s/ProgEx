package com.knabria.jiris.progex;

/**
 * Created by jiris on 27.09.2015.
 */
public class Transaction {

    private int id;
    private int amountInAccountCurrency;
    private String direction;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmountInAccountCurrency() {
        return amountInAccountCurrency;
    }

    public void setAmountInAccountCurrency(int amountInAccountCurrency) {
        this.amountInAccountCurrency = amountInAccountCurrency;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
