package org.howard.edu.lsp.assignment3;

import java.util.*;

/**
 * Coordinates the ETL (Extract-Transform-Load) pipeline.
 * Orchestrates reading input data, applying transformations,
 * and writing the output file. Also prints a run summary.
 */
public class ETLPipeline {
	/**
     * Main entry point for the ETL pipeline.
     *
     * @param args command line arguments (not used)
     */
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
            int rowsSkipped = extractor.getRowsSkipped();
            int rowsTransformed = rowsRead - rowsSkipped;

            /**
             * Prints a summary of the run including rows read, transformed, and skipped.
             *
             * @param rowsRead number of rows read from input
             * @param rowsTransformed number of rows successfully transformed
             * @param rowsSkipped number of rows skipped due to errors
             * @param outputPath path of the written output file
             */
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
