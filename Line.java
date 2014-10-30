package jubjub_robofab;

public class Line {
    double m;
    double b;

    public Line( Point a, Point b ) {
	m = a.slope(b);
	this.b = a.y - m * a.x;
    }

    public Line( double slope, double intercept ) {
	m = slope;
	b = intercept;
    }

    public Line( Point p, double slope ) {
	m = slope;
	b = p.y - m * p.x;
    }

    // the perpendicular line to this one at point p
    public Line perpendicular( Point p ) {
	return new Line(p,-1/m);
    }

    public String toString() {
	return String.format("[Line m(%.03f) b(%.03f)]",m, b);
    }

    public double y( double x ) {
	return m * x - b;
    }

    public double x( double y ) {
	return ( y - b ) / m;
    }
}
