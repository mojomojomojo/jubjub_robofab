package jubjub_robofab;

import java.awt.Graphics2D;

public class Circle {
    Point center;
    Point LL,UR;
    double radius,width,height;

    public Circle( Point c, double r ) {
	center = new Point(c.x,c.y);
	radius = r;
	height = width = 2*radius;
	LL = new Point(center.x-r,center.y-r);
	UR = new Point(center.x+r,center.y+r);
    }

    public String toString() {
        return String.format("[Circle C%s R(%.1f)]",center,radius);
    }

    // public Circle( Point ll, Point ur ) {
    // 	LL = new Point(ll.x,ll.y);
    // 	UR = new Point(ur.x,ur.y);
    // 	center = new Point((LL.x+UR.x)/2,(LL.y+UR.y)/2);
    // 	width = Math.abs(UR.x-LL.x);
    // 	height = Math.abs(UR.y-LL.y);
    // 	radius = width/2; // error checking?
    // }

    public void draw( Graphics2D g ) {
	g.drawOval((int)LL.x,(int)LL.y,(int)width,(int)height);
    }
    public void fill( Graphics2D g ) {
	g.fillOval((int)LL.x,(int)LL.y,(int)width,(int)height);
    }

    public Point[] intersect( Circle c ) {

        //*System.out.printf("[CIRCLE_ISECT] C1:%s; C2:%s\n",this,c);

        // Based on info obtained from Wolfram
        //   (http://mathworld.wolfram.com/Circle-CircleIntersection.html)
        // Reorient the two circles so that their centers are both on the
        //   x-axis and one center is at the origin.
        //
        //   x^2 + y^2 = r1^2
        //   (x-d)^2 + y^2 = r2^2
        //
        //   x = (d^2-r2^2+r1^2)/(2d)
        //   y^2 = (4 d^2 r1^2 - (d^2 - r2^2 + r1^2)^2) / (4 d^2)
        //
        // Once the two intersection points are found, rotate and translate
        //   them back to the original cartesian view.

        // The amount to rotate after the calculations.
        double undoAngle = Math.toDegrees(Math.atan2(c.center.y-center.y,
                                                     c.center.x-center.x));
        //*System.out.printf("[CIRCLE_ISECT] angle(%.3f)\n",undoAngle);

        double d = center.distance(c.center);
        //*System.out.printf("[CIRCLE_ISECT] d(%.3f)\n",d);
        double overlap = radius+c.radius - d;
        //*System.out.printf("[CIRCLE_ISECT] overlap(%.3f)\n",overlap);
        if (overlap < 0) {
            // no intersection

            return new Point[0];
        }

        double Q = ( d*d - c.radius*c.radius + radius*radius );
        double x =  Q / ( 2 * d);

        double y2 = ( 4*d*d*radius*radius - Q*Q ) / (4 * d*d);
        //*System.out.printf("[CIRCLE_ISECT] Q(%.3f) x(%.3f) y^2(%.3f)\n",Q,x,y2);

        if (Math.abs(y2) < 0.01) {
            return new Point[]{
                new Point(x,0).rotateTranslate(Point.ORIGIN, undoAngle, center)
            };
        }

        double y = Math.sqrt(y2);
        //*System.out.printf("[CIRCLE_ISECT] I1(%.3f,%.3f) I2(%.3f,%.3f)\n",x,y,x,-y);
        return new Point[]{
            new Point(x,y).rotateTranslate(Point.ORIGIN, undoAngle, center),
            new Point(x,-y).rotateTranslate(Point.ORIGIN, undoAngle, center),
        };

    }
}
