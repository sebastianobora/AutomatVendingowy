package sample.model;

public abstract class CoinFactory {
    public static final Double[] valuesTab = {0.01, 0.02, 0.05, 0.10, 0.20, 0.50, 1.00, 2.00, 5.00};

    public abstract Coin makeCoin(double nominal, int quantity);
}

