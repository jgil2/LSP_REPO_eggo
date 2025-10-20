package org.howard.edu.lsp.midterm.question2;

public class AreaCalculator {

	public static void main(String[] args) {
		try {
			System.out.println("Circle area: " + area(3.0));
            System.out.println("Rectangle area: " + area(5.0, 2.0));
            System.out.println("Triangle area: " + area(10, 6));
            System.out.println("Square area: " + area(4)); 
			
		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
		}

	}
	
	// Circle area
	public static double area(double radius) {
		if (radius <= 0) {
			throw new IllegalArgumentException("Radius should be greater than 0.");
		}
			return Math.PI * Math.pow(radius, 2);
	}

	// Rectangle area
	public static double area(double width, double height) {
		if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("The width and height values must be greater than 0.");
        }
        return width * height;
	}

	// Triangle (base & height) area
	public static double area(int base, int height) {
		if (base <= 0 || height <= 0) {
            throw new IllegalArgumentException("The base and height values must be greater than 0.");
        }
        return 0.5 * base * height;
	}

	// Square (side length) area
	public static double area(int side) {
		 if (side <= 0) {
	            throw new IllegalArgumentException("The side length must be greater than 0.");
	        }
	        return Math.pow(side, 2);
	}

}
