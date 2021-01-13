package sample.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoinTest {

    private String message;

    private final Double[] validDenominations = Coin.getDenominations();
    private final int validQuantity = 5;

    private final double invalidNominal = 0.15;
    private final int invalidQuantity = -4;

    private final int decrementQuantity = 3;

    @Test
    void newCoinValidData() {

        for(double nominal : validDenominations){
            try{
                Coin coin = new Coin(nominal, validQuantity);
                message = "Properly created Coin: " + coin.toString() + " : " + coin.getQuantity() + " using valid data";
                System.out.println(message);
            }catch(IllegalArgumentException e){
                String message = "Coin created using valid data thrown exception: " + e.getMessage();
                fail(message);
            }
        }
    }

    @Test
    void newCoinInvalidNominal(){
            try{
                Coin coin = new Coin(invalidNominal, validQuantity);
                message = "Coin created using invalid data didn't thrown exception";
                fail(message);
            }catch(IllegalArgumentException e){
                message = "Properly thrown exception because of invalid data: " + e.getMessage();
                System.out.println(message);
            }
    }

    @Test
    void newCoinInvalidQuantity(){
        try{
            Coin coin = new Coin(validDenominations[0], invalidQuantity);
            message = "Coin created using invalid data didn't thrown exception";
            fail(message);
        }catch(IllegalArgumentException e){
            message = "Properly thrown exception because of invalid data: " + e.getMessage();
            System.out.println(message);
        }
    }

    @Test
    void incrementQuantity() {
        int expectedQuantity = validQuantity + 1;
        Coin coin = new Coin(validDenominations[0], validQuantity);

        System.out.println("Actual quantity : " + coin.getQuantity());
        System.out.println("Expected quantity: " + expectedQuantity);

        coin.incrementQuantity();

        System.out.println("After incrementQuantity()" + " : " + coin.getQuantity());
        assertEquals(coin.getQuantity(), expectedQuantity);
    }

    @Test
    void decrementQuantity() {
        int expectedQuantity = validQuantity - decrementQuantity;
        Coin coin = new Coin(validDenominations[0], validQuantity);

        System.out.println("Actual quantity : " + coin.getQuantity());
        System.out.println("Expected quantity: " + expectedQuantity);

        coin.decrementQuantity(decrementQuantity);

        System.out.println("After decrementQuantity(" + decrementQuantity +")" + " : " + coin.getQuantity());
        assertEquals(coin.getQuantity(), expectedQuantity);
    }

    @Test
    void getCopy(){
        Coin coin = new Coin (validDenominations[0], validQuantity);
        Coin coinCopy = coin.getCopy();

        System.out.println("Coin -> " + coin.hashCode());
        System.out.println("CopyCoin -> " + coinCopy.hashCode());

        assertNotSame(coin, coinCopy);
    }
}