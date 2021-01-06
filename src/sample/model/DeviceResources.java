package sample.model;

import sample.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;

public class DeviceResources {
    private static final DeviceResources INSTANCE = new DeviceResources();
    private final ArrayList<Product> products;
    private final ArrayList<Coin> coins;

    public DeviceResources() {
        products = new ArrayList<>();
        coins = new ArrayList<>();
    }

    public static DeviceResources getInstance() {
        return INSTANCE;
    }

    public boolean isUniqueProduct(String data, ArrayList<Integer> uniqueProducts){
        return !uniqueProducts.contains(Integer.parseInt(data));
    }

    public boolean isDouble(String data){
        try{
            Double.parseDouble(data);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public boolean properNameLength(String productName){
        return productName.length() <= Constants.MAX_PRODUCT_NAME_LENGTH;
    }

    public boolean properQuantity(String data){
        try{
            return Integer.parseInt(data) >= 0 && Integer.parseInt(data) <= Constants.MAX_PRODUCT_QUANTITY;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public boolean isUniqueCoin(String data, ArrayList<Double> uniqueCoins){
        return !uniqueCoins.contains(Double.parseDouble(data));
    }

    public boolean properCoinQuantity(String quantity){
        try{
            return Integer.parseInt(quantity) >= 0 && Integer.parseInt(quantity) <= Constants.MAX_COIN_QUANTITY;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public void setProducts(ArrayList<String[]> allProducts) {
        Iterator<String[]> productsIterator = allProducts.iterator();
        ArrayList<Integer> uniqueProducts = new ArrayList<>();
        if(allProducts.isEmpty()){
            throw new IllegalArgumentException("No products found!");
        }
        for (int i = 0; i < Constants.MAX_PRODUCTS; i++) {
            if (productsIterator.hasNext()) {
                String[] data = productsIterator.next();

                if (isUniqueProduct(data[0], uniqueProducts) && properNameLength(data[1]) && isDouble(data[2]) && properQuantity(data[4])) {
                    uniqueProducts.add(Integer.parseInt(data[0]));
                    Product product =
                            new Product(data[0], data[1], Double.parseDouble(data[2]), data[3], Integer.parseInt(data[4]));
                    products.add(product);
                } else {
                    products.clear();
                    throw new IllegalArgumentException("Wrong product data!");
                }
            } else {
                break;
            }
        }
        products.sort(Product::compareTo);
    }

    public void setCoins(ArrayList<String[]> allCoins) {
        Iterator<String[]> coinsIterator = allCoins.iterator();
        ArrayList<Double> uniqueCoins = new ArrayList<>();
        if(allCoins.isEmpty()){
            throw new IllegalArgumentException("No coins found!");
        }

        for (int i = 0; i < Constants.MAX_COINS; i++) {
            if (coinsIterator.hasNext()) {
                String[] data = coinsIterator.next();
                if (isUniqueCoin(data[0], uniqueCoins) && properCoinQuantity(data[1])) {
                    uniqueCoins.add(Double.parseDouble(data[0]));
                    Coin coin = new Coin(Double.parseDouble(data[0]), Integer.parseInt(data[1]));
                    coins.add(coin);
                }else {
                    coins.clear();
                    throw new IllegalArgumentException("Wrong coins data!");
                }
            } else {
                break;
            }
        }

        coins.sort(Coin::compareTo);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public Product findProductBySlot(String slot) {
        for (Product product : products) {
            if (slot.equals(product.getSlot())) {
                return product;
            }
        }
        return null;
    }

    public boolean isWithdrawPossible(double change) {
        ArrayList<Coin> coinsCopy = new ArrayList<>();
        for (Coin coin : coins) {
            coinsCopy.add(coin.getCopy());
        }

        for (int i = coinsCopy.size() - 1; i >= 0; i--) {
            int quotient = (int) (Math.floor(change / coinsCopy.get(i).getNominal()));
            if (quotient >= 1 && coinsCopy.get(i).getQuantity() >= 0) {
                if(coinsCopy.get(i).getQuantity() < quotient){
                    quotient = coinsCopy.get(i).getQuantity();
                }
                coinsCopy.get(i).decrementQuantity(quotient);
                change -= coinsCopy.get(i).getNominal() * quotient;
                change = Math.round(change * 100d) / 100d;
            }
        }
        return change == 0;
    }

    public ArrayList<Coin> getWithdraw(double change) {
        ArrayList<Coin> changeCoins = new ArrayList<>();

        for (int i = coins.size() - 1; i >= 0; i--) {
            int quotient = (int) (Math.floor(change / coins.get(i).getNominal()));
            if (quotient >= 1 && coins.get(i).getQuantity() >= 0) {
                    if(coins.get(i).getQuantity() < quotient){
                        quotient = coins.get(i).getQuantity();
                    }
                changeCoins.add(new Coin(coins.get(i).getNominal(), quotient));
                coins.get(i).decrementQuantity(quotient);
                change -= coins.get(i).getNominal() * quotient;
                change = Math.round(change * 100d) / 100d;
            }
        }
        return changeCoins;
    }
}
