package org.howard.edu.lsp.assignment3;

import java.io.*;
import java.util.*;

public class Loader {
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