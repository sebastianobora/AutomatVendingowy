package sample.model;

import sample.util.Constants;

import java.util.Arrays;

public class Coin {
    public static final Double[] valuesTab = {0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.00, 2.00, 5.00};
    private final String currency;
    private final double nominal;
    private int quantity;

    public Coin(double nominal, int quantity) {
        if(!Arrays.asList(valuesTab).contains(nominal) && quantity < 0){
            throw new IllegalArgumentException("Wrong nominal or quantity!");
        }
        this.nominal = nominal;
        this.quantity = quantity;
        this.currency = Constants.CURRENCY;
    }

    public Coin getCopy(){
        return new Coin(this.getNominal(), this.getQuantity());
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

    public void incrementQuantity(){
        this.quantity++;
    }

    public void decrementQuantity(int amount){
        this.quantity-= amount;
    }

    @Override
    public String toString() {
        return String.format("%.2f" + " " + currency, nominal);
    }
}
