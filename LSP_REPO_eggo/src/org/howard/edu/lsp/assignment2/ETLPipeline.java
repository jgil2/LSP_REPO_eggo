package org.howard.edu.lsp.assignment2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

class Products {
	private int productId;
    private String name;
    private double price;
    private String category;

    public Products(int productId, String name, double price, String category) {
        this.productId = productId;
    	this.name = name;
        this.price = price;
        this.category = category;
    }
    // methods used for transforming
    public void uppercase() { //changes all products to uppercase letters
        this.name = this.name.toUpperCase();
    }
    // Apply 10% discount to Electronics, then round to 2 decimals
    public void discount() {
    	if (this.category.equals("Electronics")) {
        	this.price = this.price - (this.price*0.10);
        }
    	
    	BigDecimal bd = new BigDecimal(this.price);
    	bd = bd.setScale(2, RoundingMode.HALF_UP);
    
    	this.price = bd.doubleValue();
    	
    }
    
    public void over500() { //changes category to Premium Electronics if the price after discount is more than 500
    	if (this.category.equals("Electronics") && this.price > 500) {
        	this.category = "Premium Electronics";
    	}
    	
    }
    
    public String priceRange() { //adds price range to products based on discounted price
    	if (price >= 0 && price <= 10) return "Low";
        else if (price >= 10.01 && price <= 100) return "Medium";
        else if (price >= 100.01 && price <= 500) return "High";
        else return "Premium";
    }
    // used to write to the csv file
    public String toCSVLine() {
        return productId + "," + name + "," + price + "," + category + "," + priceRange();
    }
    
    @Override
    public String toString() {
        return "Products{productid='" + productId + "', name='" + name + "', price=" + price + ", category='" + category + "'}";
    }
}

public class ETLPipeline {

    public static void main(String[] args ) {
    	String inputPath = "data/products.csv";
        String outputPath = "data/transformed_products.csv";
        
        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            System.out.println("Error: Input file not found at " + inputFile.getAbsolutePath());
            return; // stop the program if the file doesn't exist
        }
        
        List<Products> product = load(inputPath);
        
        if (product.isEmpty()) {
        	System.out.println("Warning: The input file has no data rows. The output file will only have the header row.");
        }
        
        for (Products p : product) {
            p.uppercase();
            p.discount();
            p.over500();
        }        
        
        saveTransformed(product, outputPath);
        System.out.println("✅ Output written to: " + outputPath);
    }

    public static List<Products> load(String path) {
        List<Products> product = new ArrayList<>();
        
        int rowsRead = 0;
        int rowsTransformed = 0;
        int rowsSkipped = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        	br.readLine(); // skip header
        	
        	String line;
            while ((line = br.readLine()) != null) {
            	rowsRead++;
            	
                String[] fields = line.split(",");
                
                if (fields.length == 4) {
                	for (int i = 0; i < fields.length; i++) {
                        fields[i] = fields[i].trim().replaceAll("^\"|\"$", "");
                    }
                	
                	int productId = Integer.parseInt(fields[0].trim());
                    String name = fields[1].trim();
                    double price = Double.parseDouble(fields[2].trim());
                    String category = fields[3].trim();
                    product.add(new Products(productId, name, price, category));
                    rowsTransformed++;
                } else {
                    System.out.println("Skipping malformed line: " + line);
                    rowsSkipped++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n--- Run Summary ---");
        System.out.println("Rows read       : " + rowsRead);
        System.out.println("Rows transformed: " + rowsTransformed);
        System.out.println("Rows skipped    : " + rowsSkipped);
        
        return product;
    }
    
    public static void saveTransformed(List<Products> product, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("ProductID,Name,Price,Category,PriceRange");
            bw.newLine();

            for (Products p : product) {
                bw.write(p.toCSVLine());
                bw.newLine();
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
    