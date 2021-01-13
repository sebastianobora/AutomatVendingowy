package sample.model;


import sample.util.Constants;

public class Product implements Comparable<Product> {
    private final String slot;
    private final String name;
    private final double price;
    private final String imgPath;
    private int quantity;

    public Product(String slot, String name, double price, String imgPath, int quantity) {
        if(name.length() > Constants.MAX_PRODUCT_NAME_LENGTH){
            throw new IllegalArgumentException("Wrong nominal!-product file");
        }
        if(quantity < 0 || quantity > Constants.MAX_PRODUCT_QUANTITY){
            throw new IllegalArgumentException("Wrong quantity!-product file");
        }
        if(price < 0 || price > Constants.MAX_PRODUCT_PRICE){
            throw new IllegalArgumentException("Wrong price!-product file");
        }
        if(Integer.parseInt(slot) < Constants.MIN_SLOT || Integer.parseInt(slot) > Constants.MAX_SLOT){
            throw new IllegalArgumentException("Wrong slot!-product file");
        }
        this.slot = slot;
        this.name = name;
        this.price = price;
        this.imgPath = imgPath;
        this.quantity = quantity;
    }

    public String getSlot() {
        return slot;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public void decrementQuantity() {
        this.quantity -= 1;
    }

    @Override
    public int compareTo(Product o) {
        return this.slot.compareTo(o.slot);
    }
}