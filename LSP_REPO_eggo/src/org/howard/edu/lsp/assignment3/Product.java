package org.howard.edu.lsp.assignment3;

public class Product {
    private int productId;
    private String name;
    private double price;
    private String category;
    private String priceRange;

    // Constructor
    public Product(int productId, String name, double price, String category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters and setters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getPriceRange() { return priceRange; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    @Override
    public String toString() {
        return productId + "," + name + "," + String.format("%.2f", price) + "," + category + "," + priceRange;
    }
}
