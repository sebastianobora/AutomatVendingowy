package sample.model;


public class Product implements Comparable<Product> {
    private final String slot;
    private final String name;
    private final double price;
    private final String imgPath;
    private int quantity;

    public Product(String slot, String name, double price, String imgPath, int quantity) {
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