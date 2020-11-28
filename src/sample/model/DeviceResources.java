package sample.model;
import sample.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;

public class DeviceResources {
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList<>();

    public DeviceResources(ArrayList<String[]> allProducts, ArrayList<String[]> allCoins) {
        Iterator<String[]> productsIterator = allProducts.iterator();
        boolean[] uniqueProducts = new boolean[Constants.MAX_PRODUCTS];

        for(int i = 0; i < Constants.MAX_PRODUCTS ; i++){
            if(productsIterator.hasNext()){
                String[] data = productsIterator.next();

                if(!uniqueProducts[Integer.parseInt(data[0])]){
                    uniqueProducts[Integer.parseInt(data[0])] = true;
                    Product product =
                            new Product(data[0], data[1], Double.parseDouble(data[2]), data[3], Integer.parseInt(data[4]));
                    products.add(product);
                }
            }else {
                break;
            }
        }

        Iterator<String[]> coinsIterator = allCoins.iterator();
        ArrayList<Double> uniqueCoins = new ArrayList<>();

        for(int i = 0; i < Constants.MAX_COINS ; i++){
            if(coinsIterator.hasNext()){
                String[] data = coinsIterator.next();
                if(!uniqueCoins.contains(Double.parseDouble(data[0]))) {
                    uniqueCoins.add(Double.parseDouble(data[0]));
                    Coin coin = new Coin(Double.parseDouble(data[0]), Integer.parseInt(data[1]));
                    coins.add(coin);
                }
            }else {
                break;
            }
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }
}
