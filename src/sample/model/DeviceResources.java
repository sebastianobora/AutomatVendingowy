package sample.model;
import sample.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;

public class DeviceResources {
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList<>();

    public DeviceResources(ArrayList<String[]> allProducts, ArrayList<String[]> allCoins){
        Iterator<String[]> productsIterator = allProducts.iterator();
        ArrayList<Integer> uniqueProducts = new ArrayList<>();

        for(int i = 0; i < Constants.MAX_PRODUCTS ; i++){
            if(productsIterator.hasNext()){
                String[] data = productsIterator.next();

                if(!uniqueProducts.contains(Integer.parseInt(data[0]))){
                    uniqueProducts.add(Integer.parseInt(data[0]));
                    Product product =
                            new Product(data[0], data[1], Double.parseDouble(data[2]), data[3], Integer.parseInt(data[4]));
                    products.add(product);
                }else{
                    throw new IllegalArgumentException("Wrong slot in .csv file!");
                }
            }else {
                break;
            }
        }
        products.sort(Product::compareTo);

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

    public void setCoins(ArrayList<Coin> coins) {
        this.coins = coins;
    }

    public Integer findProductBySlot(String slot){
        for(Product product : products){
            if(slot.equals(product.getSlot())){
                return products.indexOf(product);
            }
        }
        return null;
    }
}
