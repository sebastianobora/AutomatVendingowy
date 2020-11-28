package sample.model;

import sample.util.Constants;

public class Coin {
    private final String currency;
    private final double nominal;
    private final int quantity;

    public Coin(double nominal, int quantity) {
        this.nominal = nominal;
        this.quantity = quantity;
        this.currency = Constants.CURRENCY;
    }

    public String getCurrency() {
        return currency;
    }

    public double getNominal() {
        return nominal;
    }

    public int getQuantity() {
        return quantity;
    }
}
