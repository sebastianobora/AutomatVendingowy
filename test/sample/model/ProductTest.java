package sample.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private final String validSlot = "11";
    private final String validName = "Snickers";
    private final String validImg = "src/sample/img/productsImg/snickers.png";
    private final double validPrice = 4.5;
    private final int validQuantity = 10;
    private final int validZeroQuantity = 0;

    private final String invalidSlot = "2";
    private final String invalidName = "Too long name of product";
    private final double invalidPrice = -4.5;
    private final int invalidQuantity = -10;

    @Test
    void newProductValidData(){
        try{
            Product product = new Product(validSlot, validName, validPrice, validImg, validQuantity);
            System.out.println("Product created properly with valid data");
        }catch(IllegalArgumentException e){
            fail("Exception thrown with valid data: " + e.getMessage());
        }
    }

    @Test
    void newProductInvalidSlot(){
        try{
            Product product = new Product(invalidSlot, validName, validPrice, validImg, validQuantity);
            fail("Exception not thrown with invalid data");
        }catch(IllegalArgumentException e){
            System.out.println("Exception thrown properly with invalid data: " + e.getMessage());
        }
    }

    @Test
    void newProductInvalidName(){
        try{
            Product product = new Product(invalidSlot, invalidName, validPrice, validImg, validQuantity);
            fail("Exception not thrown with invalid data");
        }catch(IllegalArgumentException e){
            System.out.println("Exception thrown properly with invalid data: " + e.getMessage());
        }
    }

    @Test
    void newProductInvalidPrice(){
        try{
            Product product = new Product(invalidSlot, validName, invalidPrice, validImg, validQuantity);
            fail("Exception not thrown with invalid data");
        }catch(IllegalArgumentException e){
            System.out.println("Exception thrown properly with invalid data: " + e.getMessage());
        }
    }

    @Test
    void newProductInvalidQuantity(){
        try{
            Product product = new Product(invalidSlot, validName, validPrice, validImg, invalidQuantity);
            fail("Exception not thrown with invalid data");
        }catch(IllegalArgumentException e){
            System.out.println("Exception thrown properly with invalid data: " + e.getMessage());
        }
    }

    @Test
    void isAvailableNonZero() {
        Product product = new Product(validSlot, validName, validPrice, validImg, validQuantity);
        assertTrue(product.isAvailable());
    }

    @Test
    void isAvailableZero() {
        Product product = new Product(validSlot, validName, validPrice, validImg, validZeroQuantity);
        assertFalse(product.isAvailable());
    }

    @Test
    void decrementQuantity(){
        Product product = new Product(validSlot, validName, validPrice, validImg, validQuantity);
        int expectedQuantity = validQuantity - 1;

        product.decrementQuantity();
        assertEquals(product.getQuantity(), expectedQuantity);
    }
}