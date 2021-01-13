package sample.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import sample.dao.DataAccessObject;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeviceResourcesTest {

    DeviceResources deviceResources = DeviceResources.getInstance();
    DataAccessObject dataAccessObject = DataAccessObject.getInstance();

    private final String emptyProductsFilePath = "test/sample/dataTest/emptyProducts.csv";
    private final String emptyCoinsFilePath = "test/sample/dataTest/emptyCoins.csv";

    private final String changePossibleCoinsFilePath = "test/sample/dataTest/testCoinsChangePossible.csv";//1.0
    private final String changeImpossibleCoinsFilePath = "test/sample/dataTest/testCoinsChangeImpossible.csv";//0.6
    private final String productsFilePath = "test/sample/dataTest/testProducts.csv";

    private final String slot = "11";
    private final double change = 1;



    @Test
    void setProductsEmptyFile() {
        ArrayList<String[]> data = dataAccessObject.loadData(emptyProductsFilePath);
        try{
            deviceResources.setProducts(data);
            fail("Exception not thrown, though passed no data");
        }catch(IllegalArgumentException e){
            System.out.println("Properly thrown exception: " + e.getMessage());
        }
    }

    @Test
    void setCoinsEmptyFile() {
        ArrayList<String[]> data = dataAccessObject.loadData(emptyCoinsFilePath);
        try{
            deviceResources.setCoins(data);
            fail("Exception not thrown, though passed no data");
        }catch(IllegalArgumentException e){
            System.out.println("Properly thrown exception: " + e.getMessage());
        }
    }

    @Test
    void findProductBySlot() {
        deviceResources.setProducts(dataAccessObject.loadData(productsFilePath));
        Product product = deviceResources.findProductBySlot(slot);
        String expectedSlot = slot;
        assertEquals(product.getSlot(), slot);
    }

    @Test
    void isWithdrawPossibleYes() {
        deviceResources.setCoins(dataAccessObject.loadData(changePossibleCoinsFilePath));
        System.out.println("Sum of coins in device: " + deviceResources.getSumOfCoins());
        System.out.println("Required change:        " + change);
        System.out.println("Is withdraw possible?:  " + deviceResources.isWithdrawPossible(change));
        assertTrue(deviceResources.isWithdrawPossible(change));
    }

    @Test
    void isWithdrawPossibleNo() {
        deviceResources.setCoins(dataAccessObject.loadData(changeImpossibleCoinsFilePath));
        System.out.println("Sum of coins in device: " + deviceResources.getSumOfCoins());
        System.out.println("Required change:        " + change);
        System.out.println("Is withdraw possible?:  " + deviceResources.isWithdrawPossible(change));
        assertFalse(deviceResources.isWithdrawPossible(change));
    }

    @Test
    void getWithdraw(){
        deviceResources.setCoins(dataAccessObject.loadData(changePossibleCoinsFilePath));
        System.out.println("Required change: " + change);
        ArrayList<Coin> withdraw = deviceResources.getWithdraw(change);
        System.out.println("\nWithdraw: ");

        double sum = 0.0;

        for(Coin coin : withdraw){
            System.out.println(coin.getNominal() + " : " + coin.getQuantity());
            sum+= coin.getNominal() * coin.getQuantity();
            sum = Math.round(sum * 100d) / 100d;
        }
        System.out.println("\nSum of withdrawn coins: " + sum);
        assertEquals(change, sum);
    }
}