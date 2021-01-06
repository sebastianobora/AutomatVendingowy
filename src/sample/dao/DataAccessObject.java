package sample.dao;

import sample.model.Coin;
import sample.model.Product;
import sample.util.Constants;

import java.io.*;
import java.util.ArrayList;

public class DataAccessObject {
    private static final DataAccessObject INSTANCE = new DataAccessObject();

    public DataAccessObject() {
    }

    public static DataAccessObject getInstance() {
        return INSTANCE;
    }

    public ArrayList<String> CSVCoinParser(ArrayList<Coin> coins) {
        ArrayList<String> parsedCoins = new ArrayList<>();
        parsedCoins.add("nominal, quantity");
        for (Coin coin : coins) {
            parsedCoins.add(coin.getNominal() + ", " + coin.getQuantity());
        }
        return parsedCoins;
    }

    public ArrayList<String> CSVProductParser(ArrayList<Product> products) {
        ArrayList<String> parsedProduct = new ArrayList<>();
        parsedProduct.add("slot, name, price, imgPath, quantity");
        for (Product product : products) {
            parsedProduct.add(
                    product.getSlot() + ", " +
                            product.getName() + ", " +
                            product.getPrice() + ", " +
                            product.getImgPath() + ", " +
                            product.getQuantity());
        }
        return parsedProduct;
    }

    public ArrayList<String[]> loadData(String filePath) {
        ArrayList<String[]> data = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            br.readLine();
            for (int i = 0; i < Constants.MAX_PRODUCTS; i++) {
                if ((line = br.readLine()) != null) {
                    String[] values = line.split(", ");
                    data.add(values);
                }
            }
            br.close();
            return data;
        } catch (IOException e) {
            throw new IllegalArgumentException("Wrong path to .csv!");
        }
    }

    public void saveData(ArrayList<String> CSVParsedData, String filePath) {

        try {
            FileWriter fw = new FileWriter(filePath);
            for (String line : CSVParsedData) {
                fw.append(line);
                if(CSVParsedData.indexOf(line) != CSVParsedData.size() - 1){
                    fw.append("\n");
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}