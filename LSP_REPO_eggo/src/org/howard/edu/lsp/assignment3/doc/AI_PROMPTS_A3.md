Prompt: Using the files and instructions, how can I make this program more object-oriented?
Response: Great — thanks for sharing your ETLPipeline.java. Since the assignment explicitly emphasizes clean program structure and you’re asking how to make it more object-oriented (OOP), here are concrete steps you can take. I’ll outline how your current code can be refactored into an OOP design, then give you a proposed class breakdown.

Prompt from ChatGPT: Do you want me to take your existing ETLPipeline.java and refactor it into this multi-class OOP version directly so you have ready-to-run code, or would you prefer a step-by-step guide so you can restructure it gradually?
Response: Refactor please

Prompt: The application displays the error "Error: For input string: ""1""
Response: Ah — that error comes from Integer.parseInt() (or Double.parseDouble()) trying to parse a value like "1" instead of just 1. That means your CSV file has extra quotes around fields (e.g., "1","Laptop","999.99","Electronics"). Right now, the code just does line.split(",") → which keeps the quotes. 🔧 Fix Strip quotes (and surrounding whitespace) from each field before parsing.

Prompt: It doesn't handle the exception of having more columns in input data than needed
Response: Got it 👍 — right now the extractor only checks if (cols.length < 4) (too few columns) but does not handle the case where there are extra columns. That can cause parsing issues or silently ignore bad input. The assignment wants “graceful error handling,” so the extractor should: Accept exactly 4 columns (ProductID, Name, Price, Category). Skip rows with too few or too many columns. Count them in rowsSkipped.

Prompt: What are some javadocs that can be included for all the classes and public methods?
Response: Perfect question 👌 — adding Javadoc is a great way to boost clarity, readability, and professionalism in your Assignment 3 code. Below are examples of class-level and method-level Javadocs you could include for your ETL pipeline classes.