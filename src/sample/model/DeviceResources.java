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

    public boolean isUniqueCoin(String data, ArrayList<Double> uniqueCoins){
        return !uniqueCoins.contains(Double.parseDouble(data));
    }

    public boolean isDouble(String data){
        try{
            Double.parseDouble(data);
            return true;
        }catch(NumberFormatException e){
            throw new IllegalArgumentException("Wrong value type-double required!");
        }
    }

    public boolean isInt(String value){
        try{
            Integer.parseInt(value);
            return true;
        }catch(NumberFormatException e){
            throw new IllegalArgumentException("Wrong value type-int required!");
        }
    }

    public void setProducts(ArrayList<String[]> allProducts) {
        Iterator<String[]> productsIterator = allProducts.iterator();
        ArrayList<Integer> uniqueProducts = new ArrayList<>();
        products.clear();

        if(allProducts.isEmpty()){
            throw new IllegalArgumentException("No products found!");
        }
        for (int i = 0; i < Constants.MAX_PRODUCTS; i++) {
            if (productsIterator.hasNext()) {
                String[] data = productsIterator.next();

                if (isInt(data[0]) && isUniqueProduct(data[0], uniqueProducts) && isDouble(data[2]) && isInt(data[4])) {
                    uniqueProducts.add(Integer.parseInt(data[0]));
                    try{
                        Product product =
                            new Product(data[0], data[1], Double.parseDouble(data[2]), data[3], Integer.parseInt(data[4]));
                        products.add(product);
                    }catch(Exception e){
                        products.clear();
                        throw new IllegalArgumentException(e.getMessage());
                    }
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
        coins.clear();

        if(allCoins.isEmpty()){
            throw new IllegalArgumentException("No coins found!");
        }

        for (int i = 0; i < Constants.MAX_COINS; i++) {
            if (coinsIterator.hasNext()) {
                String[] data = coinsIterator.next();
                if (isUniqueCoin(data[0], uniqueCoins) && isInt(data[1])) {
                    uniqueCoins.add(Double.parseDouble(data[0]));
                    try{
                        Coin coin = new Coin(Double.parseDouble(data[0]), Integer.parseInt(data[1]));
                        coins.add(coin);
                    }catch(Exception e){
                        coins.clear();
                        throw new IllegalArgumentException(e.getMessage());
                    }
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

    public double getSumOfCoins(){
        double sum = 0;
        for(Coin coin : coins){
            sum+= coin.getNominal() * coin.getQuantity();
            sum = Math.round(sum * 100d) / 100d;
        }
        return sum;
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
            if (quotient >= 1 && coinsCopy.get(i).getQuantity() > 0) {
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
        ArrayList<Coin> withdrawCoins = new ArrayList<>();

        for (int i = coins.size() - 1; i >= 0; i--) {
            int quotient = (int) (Math.floor(change / coins.get(i).getNominal()));
            if (quotient >= 1 && coins.get(i).getQuantity() > 0) {
                    if(coins.get(i).getQuantity() < quotient){
                        quotient = coins.get(i).getQuantity();
                    }
                withdrawCoins.add(new Coin(coins.get(i).getNominal(), quotient));
                coins.get(i).decrementQuantity(quotient);
                change -= coins.get(i).getNominal() * quotient;
                change = Math.round(change * 100d) / 100d;
            }
        }
        return withdrawCoins;
    }
}
