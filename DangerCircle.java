package jubjub_robofab;

public class DangerCircle extends Circle {
    double t0;
    Point p_t0;

    public DangerCircle( Point c, double r, Point p_t0, double t0 ) {
        super(c,r);
        self.t0 = t0;
        self.p_t0 = new Point(p_t0.x,p_t0.y);
    }

    // Return the point on the circle given the time (turn number).
    public Point[] dangerousLoci( double t ) {
        
    }
}
