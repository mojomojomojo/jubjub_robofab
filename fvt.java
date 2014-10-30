package jubjub_robofab;

import robocode.*;

import java.util.Vector;
import java.util.Iterator;

public class fvt {
    static boolean debug = false;

    public static boolean test_norm() {
	double thresh = .001;

	for (double a=0; a<181; a+= 1.0) {
	    double na = Util.norm(a);
	    if (Math.abs(na-a) > thresh) {
		System.out.printf("ERROR [Util.norm()] Q1/Q2 %.1f != %.1f\n",
				  a,na);
		return false;
	    }
	}
	for (double a=181; a<361; a+= 1.0) {
	    double na = Util.norm(a);
	    double exp = a-360;
	    if (Math.abs(na-exp) > thresh) {
		System.out.printf("ERROR [Util.norm()] Q3/Q4 %.1f != %.1f\n",
				  exp,na);
		return false;
	    }
	}
	for (double a=360; a<541; a+= 1.0) {
	    double na = Util.norm(a);
	    double exp = a-360;
	    if (Math.abs(na-exp) > thresh) {
		System.out.printf("ERROR [Util.norm()] Q1/Q2 ++ %.1f != %.1f\n",
				  exp,na);
		return false;
	    }
	}
	for (double a=541; a<720; a+= 1.0) {
	    double na = Util.norm(a);
	    double exp = a-720;
	    if (Math.abs(na-exp) > thresh) {
		System.out.printf("ERROR [Util.norm()] Q1/Q2 ++ %.1f != %.1f\n",
				  exp,na);
		return false;
	    }
	}
	for (double a=-1; a>-181; a-= 1.0) {
	    double na = Util.norm(a);
	    if (Math.abs(na-a) > thresh) {
		System.out.printf("ERROR [Util.norm()] -Q4/Q3 %.1f != %.1f\n",
				  a,na);
		return false;
	    }
	}
	for (double a=-181; a>-360; a-= 1.0) {
	    double na = Util.norm(a);
	    double exp = a+360;
	    if (Math.abs(na-exp) > thresh) {
		System.out.printf("ERROR [Util.norm()] -Q2/Q1 %.1f != %.1f\n",
				  exp,na);
		return false;
	    }
	}

	return true;
    }

    public static boolean test_rotateTranslate() {
        System.out.println("[ROT_XLATE] Testing Point.rotateTranslate()...");

        class tCase {
            Point target, p, offset, exp;
            double angle;
            public tCase( double x, double y,
                          double px, double py,
                          double angle,
                          double off_x, double off_y,
                          double exp_x, double exp_y ) {
                this.target = new Point(x,y,false);
                this.p = new Point(px,py,false);
                this.angle = angle;
                this.offset = new Point(off_x,off_y,false);
                this.exp = new Point(exp_x,exp_y,false);
            }
            public String toString() {
                return String.format(
                    "(RT_TEST) %s about %s, angle(%.3f), offset %s; expected: %s",
                    target, p, angle, offset, exp );
            }
        }
        double sqrt2_2 = Math.sqrt(2)/2;
        double sqrt3_2 = Math.sqrt(3)/2;

        double dth_1 = Math.toDegrees(Math.atan(2)) - 30;
        double sin_dth_1 = Math.sin(Math.toRadians(dth_1));
        double cos_dth_1 = Math.cos(Math.toRadians(dth_1));
        double d_1 = Math.sqrt(5);

        tCase[] tests = new tCase[]{
            // simple angles
            new tCase(1,0,  0,0,  90,   0,0,   0,1),
            new tCase(1,0,  0,0,  180,   0,0,   -1,0),
            new tCase(1,0,  0,0,  270,   0,0,   0,-1),
            new tCase(1,0,  0,0,  360,   0,0,   1,0),

            new tCase(1,0,  0,0,  45,   0,0,   sqrt2_2,sqrt2_2),
            new tCase(1,0,  0,0,  135,   0,0,   -sqrt2_2,sqrt2_2),
            new tCase(1,0,  0,0,  -45,   0,0,   sqrt2_2,-sqrt2_2),
            new tCase(1,0,  0,0,  225,   0,0,   -sqrt2_2,-sqrt2_2),
            new tCase(1,0,  0,0,  315,   0,0,   sqrt2_2,-sqrt2_2),

            // slightly-less-simple angles
            new tCase(1,0,  0,0,  30,   0,0,   sqrt3_2,.5),
            new tCase(1,0,  0,0,  60,   0,0,   .5,sqrt3_2),
            new tCase(1,0,  0,0,  120,   0,0,  -.5,sqrt3_2),
            new tCase(1,0,  0,0,  150,   0,0,  -sqrt3_2,.5),
            new tCase(1,0,  0,0,  210,   0,0,  -sqrt3_2,-.5),
            new tCase(1,0,  0,0,  240,   0,0,  -.5,-sqrt3_2),
            new tCase(1,0,  0,0,  300,   0,0,  .5,-sqrt3_2),
            new tCase(1,0,  0,0,  330,   0,0,  sqrt3_2,-.5),

            // rotate around different point
            new tCase(1,0,  1,1,  90,   0,0,   2,1),
            new tCase(1,0,  1,1,  180,   0,0,  1,2),
            new tCase(1,0,  1,1,  270,   0,0,   0,1),
            new tCase(1,0,  1,1,  360,   0,0,   1,0),

            new tCase(1,6,  3,7,   30,   0,0,   3 - d_1*sin_dth_1, 7 - d_1*cos_dth_1),
            new tCase(1,6,  3,7,   30,   2,4,   2+3 - d_1*sin_dth_1, 4+7 - d_1*cos_dth_1),
        };
        
        for (int i=0; i<tests.length; i++) {
            tCase t = tests[i];

            Point result = t.target.rotateTranslate(t.p,t.angle,t.offset);
            if (fvt.debug) {
                System.out.printf("[ROT_XLATE] %s => %s\n",t,result);
            }

            if (!(Math.abs(result.x - t.exp.x) < 0.001 &&
                  Math.abs(result.y - t.exp.y) < 0.001)) {
                System.out.printf(
                    "[ROT_XLATE] Error %s; Got %s\n",
                    t, result
                                  );
                return false;
            }
        }
        System.out.printf("[ROT_XLATE] All Tests Succeed: %d\n",tests.length);
        return true;
    }

    public static boolean test_distanceTowardPoint() {
        System.out.println("[PT_TOWARD_DIST] Testing Point.distanceTowardPoint()...");

        class tCase {
            Point source, toward, exp;
            double distance;
            public tCase( double x, double y,
                          double px, double py,
                          double dist,
                          double exp_x, double exp_y ) {
                this.source = new Point(x,y,false);
                this.toward = new Point(px,py,false);
                this.distance = dist;
                this.exp = new Point(exp_x,exp_y,false);
            }
            public String toString() {
                return String.format(
                    "(PTT_TEST) %s toward %s, distance(%.3f); expected: %s",
                    source, toward, distance, exp );
            }
        }
        double sqrt2_2 = Math.sqrt(2)/2;
        double sqrt3_2 = Math.sqrt(3)/2;

        double dth_1 = Math.toDegrees(Math.atan(2)) - 30;
        double sin_dth_1 = Math.sin(Math.toRadians(dth_1));
        double cos_dth_1 = Math.cos(Math.toRadians(dth_1));
        double d_1 = Math.sqrt(5);

        tCase[] tests = new tCase[]{
            new tCase(0,0,  1,1,  10,   7.07106,7.07106),
            new tCase(4,6,  7,10, 15,   13,18),
            new tCase(4,6,  7,10, -5,   1,2),
        };
        
        for (int i=0; i<tests.length; i++) {
            tCase t = tests[i];

            Point result = t.source.distanceTowardPoint(t.toward,t.distance);
            if (fvt.debug) {
                System.out.printf("[PT_TOWARD_DIST] %s => %s\n",t,result);
            }

            if (!(Math.abs(result.x - t.exp.x) < 0.001 &&
                  Math.abs(result.y - t.exp.y) < 0.001)) {
                System.out.printf(
                    "[PT_TOWARD_DIST] Error %s; Got %s\n",
                    t, result
                                  );
                return false;
            }
        }
        System.out.printf("[PT_TOWARD_DIST] All Tests Succeed: %d\n",tests.length);
        return true;
    }

    public static String PointArrayToString( Point[] points ) {
        StringBuffer ret = new StringBuffer("[");
        for (int i=0; i<points.length; i++) {
            ret.append(String.format(" %s",points[i]));
        }
        ret.append("]");
        return ret.toString();
    }

    public static boolean test_circleIntersection() {
        System.out.println("[CIRCLE_ISECT] Testing Circle.intersect()...");
        class tCase {
            double h1,k1,r1, h2,k2,r2, root1x,root1y, root2x,root2y;
            public tCase( double h1, double k1, double r1,
                          double h2, double k2, double r2,
                          double root1x, double root1y,
                          double root2x, double root2y ) {
                this.h1 = h1; this.k1 = k1; this.r1 = r1;
                this.h2 = h2; this.k2 = k2; this.r2 = r2;
                this.root1x = root1x; this.root1y = root1y;
                this.root2x = root2x; this.root2y = root2y;
            }
            public int numRoots() {
                int total = 0;
                if (root1x != Double.POSITIVE_INFINITY) {
                    total++;
                }
                if (root2x != Double.POSITIVE_INFINITY) {
                    total++;
                }
                return total;
            }
            public double threshhold() {
                double min = Double.POSITIVE_INFINITY;
                if (Math.abs(h1) > 0.0001) min = Math.min(min,h1);
                if (Math.abs(k1) > 0.0001) min = Math.min(min,k1);
                if (Math.abs(r1) > 0.0001) min = Math.min(min,r1);
                if (Math.abs(h2) > 0.0001) min = Math.min(min,h2);
                if (Math.abs(k2) > 0.0001) min = Math.min(min,k2);
                if (Math.abs(r2) > 0.0001) min = Math.min(min,r2);
                return min * 0.0001;
            }
            public String toString() {
                return String.format(
                    "C1(%.1f,%.1f,%.1f) C2(%.1f,%.1f,%.1f) R1(%.1f,%.1f) R2(%.1f,%.1f)",
                    h1,k1,r1,   h2,k2,r2,   root1x,root1y,   root2x,root2y);
            }
        }

        Vector<tCase> tests = new Vector<tCase>();
        tests.add(new tCase(20,20,56,
                            120,100,77,
                            50.423, 67.016,
                            72.547, 39.360
                            ));
        tests.add(new tCase(120,100,56,
                            20,20,77,
                            67.453, 80.640,
                            89.577, 52.984
                            ));
        // no intersection
        tests.add(new tCase(120,100,8,
                            20,20,10,
                            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY
                            ));

        // tangent at one point
        //   d = 100
        tests.add(new tCase(50,120,60,
                            110,40,40,
                            86,72,
                            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY
                            ));

        for (Iterator<tCase> i=tests.iterator(); i.hasNext();) {
            tCase t = i.next();
            Circle c1 = new Circle(new Point(t.h1,t.k1),t.r1),
                   c2 = new Circle(new Point(t.h2,t.k2),t.r2);
            Point[] isect = c1.intersect(c2);
            double thresh = t.threshhold();

            if (fvt.debug) {
                System.out.printf("[CIRCLE_ISECT] %s => %s\n",
                                  t,fvt.PointArrayToString(isect));
            }

            if (isect.length != t.numRoots()) {
                System.out.printf("[CIRCLE_ISECT] Error: Mismatch on count of intersection points: Expected(%d) Got(%d)\n",t.numRoots(),isect.length);
                return false;
            }

            if (isect.length == 0) {
                continue;
            }

            if (isect.length == 1) {
                if (!(t.root2x == Double.POSITIVE_INFINITY)) {
                    System.out.printf("[CIRCLE_ISECT] Error: got one intersect point %s but expected: %s",isect[0],t);
                    return false;
                }
                if (!((Math.abs(t.root1x-isect[0].x) < thresh) &&
                      (Math.abs(t.root1y-isect[0].y) < thresh))) {
                    System.out.printf("[CIRCLE_ISECT] Error: got one intersect point %s but it didn't match: %s",isect[0],t);
                    return false;
                }

            } else if (isect.length == 2) {
                // order the returned points
                if (isect[0].x > isect[1].x ||
                    (Math.abs(isect[0].x-isect[1].x)<thresh &&
                     isect[0].y > isect[1].y)) {
                    Point p = isect[0];
                    isect[0] = isect[1];
                    isect[1] = p;
                }

                if (!((Math.abs(t.root1x-isect[0].x) < thresh) &&
                      (Math.abs(t.root1y-isect[0].y) < thresh))) {
                    System.out.printf("[CIRCLE_ISECT] Error: got two intersect points, #1, %s, doesn't match: %s",isect[0],t);
                    return false;
                }
                if (!((Math.abs(t.root2x-isect[1].x) < thresh) &&
                      (Math.abs(t.root2y-isect[1].y) < thresh))) {
                    System.out.printf("[CIRCLE_ISECT] Error: got two intersect points, #1, %s, doesn't match: %s",isect[1],t);
                    return false;
                }
            }
        }

        System.out.printf("[CIRCLE_ISECT] All Tests Succeed: %d\n",tests.size());
        return true;
    }

    public static boolean test_arc() {
        class tCase {
	    Point c, from, to;
	    Point exp_rC;
	    double exp_angle, exp_p, exp_rtEff;
	    public tCase( Point c, Point from, Point to,
			  Point exp_rC, double exp_angle,
			  double exp_p, double exp_rtEff ) {
		this.c = c;
		this.to = to; this.from = from;
		this.exp_rC = exp_rC;
		this.exp_angle = exp_angle;
		this.exp_p = exp_p;
		this.exp_rtEff = exp_rtEff;
	    }
	    public String toString() {
		return String.format(
		           "C%s From%s To%s => rC%s angle(%.02f) p(%.01f) rtEff(%.02f)",
			   c,from,to,exp_rC,exp_angle,exp_p,
			   exp_rtEff);
	    }
	}

        tCase[] tests = new tCase[]{
            new tCase(new Point(200,200), // center
		      new Point(200,100), // from
		      new Point(300,200), // to
		      new Point(200,200), 90, .6, 2.3 ),
        };
	
        for (int i=0; i<tests.length; i++) {
            tCase t = tests[i];
	    CircularMovementModule.Arc arc =
		new CircularMovementModule.Arc(t.c,t.from,t.to);

	    if (!arc.rCenter.equals(t.exp_rC)) {
		System.out.printf("[ARC] rCenter mismatch %s != %s\n%s\n",
				  arc.rCenter,t.exp_rC,arc);
		return false;
	    }
	    if (Math.abs(arc.turnAngle - t.exp_angle) > 0.001) {
		System.out.printf("[ARC] turnAngle mismatch %s != %s\n%s\n",
				  arc.turnAngle,t.exp_angle,arc);
		return false;
	    }
	    if (Math.abs(arc.p - t.exp_p) > 0.01) {
		System.out.printf("[ARC] p mismatch %.02f != %.02f\n%s\n",
				  arc.p,t.exp_p,arc);
		return false;
	    }

	}

	return true;
    }


    public static void main( String[] args ) {
	test_norm();
        //fvt.debug = true;
        test_rotateTranslate();
        test_distanceTowardPoint();
        test_circleIntersection();
        test_arc();
    }
}
