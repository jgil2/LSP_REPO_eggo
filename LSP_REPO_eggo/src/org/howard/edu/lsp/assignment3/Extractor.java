package org.howard.edu.lsp.assignment3;

import java.io.*;
import java.util.*;

/**
 * Handles extracting product data from a CSV file.
 * Reads the file line by line and converts each row into a Product object.
 */

public class Extractor {
    private int rowsRead = 0;
    private int rowsSkipped = 0;
    
    /**
     * Reads products from a CSV file and returns them as a list.
     *
     * @param inputPath relative path to the input CSV file
     * @return list of Product objects extracted from the file
     * @throws IOException if the file cannot be read
     */

    public List<Product> extract(String inputPath) throws IOException {
        List<Product> products = new ArrayList<>();
        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            throw new FileNotFoundException("The file " + inputPath + " does not exist.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String header = reader.readLine(); // skip header
            if (header == null) return products; // empty file case

            String line;
            while ((line = reader.readLine()) != null) {
                rowsRead++;
                String[] cols = line.split(",");

                // enforce exactly 4 columns
                if (cols.length != 4) {
                	System.out.println("Skipping malformed line: " + line);
                    rowsSkipped++;
                    continue;
                }

                // Trim and strip quotes
                for (int i = 0; i < cols.length; i++) {
                    cols[i] = cols[i].trim().replaceAll("^\"|\"$", "");
                }

                try {
                    int id = Integer.parseInt(cols[0]);
                    String name = cols[1];
                    double price = Double.parseDouble(cols[2]);
                    String category = cols[3];
                    products.add(new Product(id, name, price, category));
                } catch (NumberFormatException e) {
                	System.out.println("Skipping malformed line: " + line);
                    rowsSkipped++;
                }
            }
        }
        return products;
    }

    public int getRowsRead() { return rowsRead; }
    public int getRowsSkipped() { return rowsSkipped; }
}
