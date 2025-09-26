package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Applies transformations to Product objects according to business rules.
 * Handles name formatting, discounts, recategorization, and price range calculation.
 */

public class Transformer {
	/**
     * Applies all transformations to the given product.
     *
     * @param product the product to transform
     */
    public void transform(List<Product> products) {
        for (Product p : products) {
            // 1. Uppercase name
            p.setName(p.getName().toUpperCase());

            double originalPrice = p.getPrice();

            // 2. Apply discount for Electronics
            if (p.getCategory().equalsIgnoreCase("Electronics")) {
                double discounted = round(originalPrice * 0.9);
                p.setPrice(discounted);

                // 3. Premium Electronics
                if (discounted > 500.0) {
                    p.setCategory("Premium Electronics");
                }
            }

            // 4. Determine PriceRange
            p.setPriceRange(determinePriceRange(p.getPrice()));
        }
    }

    private double round(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    /**
     * Determines the price range category for a given price.
     *
     * @param price the final product price
     * @return a string label ("Low", "Medium", "High", "Premium")
     */
    private String determinePriceRange(double price) {
        if (price <= 10.00) return "Low";
        if (price <= 100.00) return "Medium";
        if (price <= 500.00) return "High";
        return "Premium";
    }
}
