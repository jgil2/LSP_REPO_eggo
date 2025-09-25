package org.howard.edu.lsp.assignment3;

import java.util.*;

public class ETLPipeline {
    public static void main(String[] args) {
        String inputPath = "data/products.csv";
        String outputPath = "data/transformed_products.csv";

        Extractor extractor = new Extractor();
        Transformer transformer = new Transformer();
        Loader loader = new Loader();

        try {
            List<Product> products = extractor.extract(inputPath);

            if (products.isEmpty()) {
                System.out.println("Input file is empty (only header). Output will only contain header.");
            }

            transformer.transform(products);
            loader.load(products, outputPath);

            int rowsRead = extractor.getRowsRead();
            int rowsTransformed = products.size();
            int rowsSkipped = extractor.getRowsSkipped();

            System.out.println("=== Run Summary ===");
            System.out.println("Rows read: " + rowsRead);
            System.out.println("Rows transformed: " + rowsTransformed);
            System.out.println("Rows skipped: " + rowsSkipped);
            System.out.println("Output written to: " + outputPath);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
