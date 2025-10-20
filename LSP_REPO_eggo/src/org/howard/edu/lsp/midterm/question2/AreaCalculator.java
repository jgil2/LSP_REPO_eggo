package org.howard.edu.lsp.midterm.question2;

public class AreaCalculator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	// Circle area
	public static double area(double radius) {
		double circleArea = Math.PI * Math.pow(radius, 2);
		return circleArea;
	}

	// Rectangle area
	public static double area(double width, double height) {
		double rectangleArea = width * height;
		return rectangleArea;
	}

	// Triangle (base & height) area
	public static double area(int base, int height) {
		double triangleArea = (1/2) * base * height;
		return triangleArea;
	}

	// Square (side length) area
	public static double area(int side) {
		double squareArea = Math.pow(side, side);
		return squareArea;
	}

}
