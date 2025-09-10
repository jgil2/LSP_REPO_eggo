**_Overview_**

This program is a simple implementation of the ETL pipeline using Java. It reads the products file, applies transformations to certain columns and then writes that data to a new CSV file. At the end of execution, a run summary including rows read, transformed, skipped and the output path the new file is written to is printed.

**_How to Run_**

The input file is data/products.csv.

Compile and run the program from the project root.

The output file should be written to data/transformed_products.csv or data/transformed_products_empty.csv if the input file is empty.

**_Transformations_**

The following transformations are done in order:

1. Uppercase: The product names are changed to uppercase.

2. Discount for Electronics: For products under Electronics category, a 10% discount is applied to the price. Those prices are rounded to 2 decimal places with BigDecimal.

3. Premium Electronics: If the post-discount price is over 500 for electronics, the category is changed to Premium Electronics.

4. Price Range Classification: A new column called PriceRange is added based on the price:

0–10 → Low

10.01–100 → Medium

100.01–500 → High

500+ → Premium 

**_Error Handling_**

Missing input file - It prints an error message and stops the program.

Empty input file - It prints a warning to the user and writes only the header row to transformed_products_empty.csv.

Malformed row - It skips the row, prints a warning with the full row content and is counted in the run summary.

**_Assumptions_**

The input file always contains a header row.

ProductID and Price are expected to be numeric.

Only relative paths (data/...) are used, so that the program works across environments.

**_Design Notes_**

The Products class encapsulates product data and transformation logic.

The ETL steps are separated into load, transformation methods (uppercase, discount, over500, priceRange), and saveTransformed to match Extract–Transform–Load.

Run Summary: At the end of execution, the program prints:
- Rows read
- Rows transformed
- Rows skipped
- Output file path

**_Testing_**

The program was tested under several scenarios:

Normal case: Multiple valid rows - Output file contains all rows with transformations applied.

Empty file: Input file contains only headers - Output is written to transformed_products.csv.

Malformed row: The row is skipped, prints a warning with the line and is counted in summary.

Missing file: Deleted or products.csv not present - Program prints an error and exits.

**_AI Usage_**

I used ChatGPT for to help with fixing errors if presented, explaining java concepts that were unfamiliar and checking if the program was acceptable for grading and checked off every item on the checklist.

One prompt used was 'How do I create a csv file under a directory in Eclipse IDE?' I used that response to create the products file under the data folder.

AI response:
```
Right-click your project in the Package Explorer.
Example: LSP_Fall_2025

Select:
New → Folder

Give it a name, e.g., data or resources.

Click Finish.

Right-click the new folder →
New → File

Enter a filename with .csv extension, e.g.:
students.csv

Click Finish.

Eclipse will create an empty CSV file inside your folder.

You can now double-click it to edit contents.
```

**_External Internet Sources_**

Link: https://stackoverflow.com/questions/58272300/how-do-i-read-a-csv-file-with-java

Usage: Used approach for reading a csv file without etl libraries.


Link: https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java

Usage: Used approach to rounding numbers with BigDecimal with RoundingMode.HALF_UP.


Link: https://www.w3schools.com/java/java_try_catch.asp

Usage: Used for understanding how to create error messages in java.


Link: https://examples.javacodegeeks.com/checking-if-a-file-is-empty-in-java/

Usage: Used approach for checking if a file is empty.
