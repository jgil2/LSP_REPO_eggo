package org.howard.edu.lsp.assignment3;

import java.io.*;
import java.util.*;

/**
 * Handles writing transformed products into a new CSV file.
 * Ensures the output file contains a header and transformed rows.
 */
public class Loader {
	/**
     * Writes a list of products to a CSV file.
     *
     * @param outputPath relative path to the output CSV file
     * @param products the list of transformed Product objects
     * @throws IOException if the file cannot be written
     */
    public void load(List<Product> products, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("ProductID,Name,Price,Category,PriceRange\n");

            for (Product p : products) {
                writer.write(p.toString());
                writer.newLine();
            }
        }
    }
}