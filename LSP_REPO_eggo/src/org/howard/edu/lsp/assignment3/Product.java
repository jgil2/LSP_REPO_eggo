package org.howard.edu.lsp.assignment3;

/**
 * Represents a single product entry from the CSV file.
 * Encapsulates all fields related to a product and provides
 * getter/setter methods for transformations.
 */

public class Product {
    private int productId;
    private String name;
    private double price;
    private String category;
    private String priceRange;

    /**
     * Constructs a Product object with its initial values.
     *
     * @param productId the product ID
     * @param name the product name
     * @param price the product price
     * @param category the product category
     */
    public Product(int productId, String name, double price, String category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters and setters
    /** @return the product ID */
    public int getProductId() { return productId; }
    /** @return the product name */
    public String getName() { return name; }
    /** @return the product price */
    public double getPrice() { return price; }
    /** @return the product category */
    public String getCategory() { return category; }
    /** @return the derived price range (Low, Medium, High, Premium) */
    public String getPriceRange() { return priceRange; }

    /** Updates the product name. */
    public void setName(String name) { this.name = name; }
    /** Updates the product price. */
    public void setPrice(double price) { this.price = price; }
    /** Updates the product category. */
    public void setCategory(String category) { this.category = category; }
    /** Updates the derived price range. */
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }
    
    /**
     * Returns a string representation of the product as a CSV row.
     *
     * @return the CSV-formatted string of the product
     */

    @Override
    public String toString() {
        return productId + "," + name + "," + String.format("%.2f", price) + "," + category + "," + priceRange;
    }
}
