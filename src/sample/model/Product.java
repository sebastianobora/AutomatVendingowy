package sample.model;


import sample.util.Constants;

public class Product implements Comparable<Product>{
    private final String slot;
    private final String name;
    private final double price;
    private final String imgPath;
    private final int quantity;
    private final String currency;

    //TODO: zabezpieczenia związane z price, currency, slot
    public Product(String slot, String name, double price, String imgPath, int quantity) {
            this.slot = slot;
            this.name = name;
            this.price = price;
            this.imgPath = imgPath;
            this.quantity = quantity;
            this.currency = Constants.CURRENCY;
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

    @Override
    public int compareTo(Product o) {
        return this.slot.compareTo(o.slot);
    }
}