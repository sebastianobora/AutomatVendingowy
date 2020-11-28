package sample.model;

import java.util.Arrays;

public class ConcreteCoinFactory extends CoinFactory{
    @Override
    public Coin makeCoin(double nominal, int quantity) {
        if(!Arrays.asList(valuesTab).contains(nominal)){
            throw new IllegalArgumentException("Wrong nominal or currency!");
        }
        return new Coin(nominal, quantity);
    }
}
