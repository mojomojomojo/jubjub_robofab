package jubjub_robofab;

import java.awt.Graphics2D;

public class Point {
    static final Point ORIGIN = new Point(0,0);

    double x;
    double y;
    boolean integral;

    public Point( double x, double y, boolean integral ) {
        this.x = x;
        this.y = y;
        integral = integral;
    }
    public Point( double x, double y ) {
        this.x = x;
        this.y = y;
        integral = true;
    }
    public Point( Point p ) {
        this.x = p.x;
        this.y = p.y;
        this.integral = p.integral;
    }

    public boolean equals( Point p ) {
	return Math.abs(x-p.x) < 0.01 &&
	       Math.abs(y-p.y) < 0.01    ;
    }

    public String toString() {
        if (integral) {
            return String.format("(%.0f,%.0f)",x,y);
        } else {
            return String.format("(%.03f,%.03f)",x,y);
        }
    }

    public double distance( Point p ) {
	return Math.sqrt(Math.pow(x-p.x,2) + Math.pow(y-p.y,2));
    }

    public Point midpoint( Point p ) {
	return midpoint(p,.5);
    }

    public Point midpoint( Point p, double position ) {
	return new Point(x + (p.x-x)*position,
			 y + (p.y-y)*position);
    }

    public void drawLine( Graphics2D g, Point p ) {
	g.drawLine((int)x,(int)y,(int)p.x,(int)p.y);
    }

    // Rotate this about p (positive angle is rotation from +x-axis
    //   to +y-axis), then translate by offset.
    public Point rotateTranslate( Point p, double angle, Point offset ) {
        double
            sin = Math.sin(Math.toRadians(angle)),
            cos = Math.cos(Math.toRadians(angle));

        // translate p to the origin
        double x = this.x - p.x,
               y = this.y - p.y;

        double x_ = cos * x - sin * y,
               y_ = sin * x + cos * y;

        // translate back to p + offset
        return new Point( x_ + p.x + offset.x,
                          y_ + p.y + offset.y,
                          false );
    }

    public double slope( Point p ) {
	return (p.y-y)/(p.x-x);
    }

    public Point distanceTowardPoint( Point p_to, double dist ) {
        // Translate myself to the origin and rotate the frame of reference
        //   so that the two points lie on the x-axis.

        return new Point(dist,0).rotateTranslate(
            ORIGIN,
            Math.toDegrees(Math.atan2(p_to.y-y,p_to.x-x)),
            this);
    }
}
