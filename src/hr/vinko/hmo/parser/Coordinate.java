package hr.vinko.hmo.parser;

public class Coordinate {
	public final double x, y;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static double euclideanDistance(Coordinate c1, Coordinate c2) {
			return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
	}
	
}
