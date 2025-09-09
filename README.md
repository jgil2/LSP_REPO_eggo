This program is a simple implementation of the ETL pipeline using Java. It reads the products file, applies transformations to certain columns and then writes that data to a new CSV file.

**_How to Run_**

The input file is data/products.csv.

Compile and run the program from the project root.

The output file should be written to data/transformed_products.csv or data/transformed_products_empty.csv if the input file is empty.

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
Usage: Used approach to rounding numbers with BigDecimal with RoundingMode.HALF_UP

Link: https://www.w3schools.com/java/java_try_catch.asp
Usage: Used for understanding how to create error messages in java
