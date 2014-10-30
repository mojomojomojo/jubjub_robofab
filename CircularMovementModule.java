package jubjub_robofab;

import robocode.*;

import java.util.Vector;

public class CircularMovementModule extends MovementModule {
    Point center; // the point around which we will revolve.

    public static class Arc {
        Point center;
	Point rCenter; // the effective center of revolution
        Point from;
        Point to;

        // R = (360 * v)/(10-3/4*v)/2pi
        // R = (360 * v)/turn_rate/2pi

        double radius;
	double turnAngle;
	double turnRateEff;
	double turnA, turnB; // (turnA+turnB)/2 = turnRateEff
	double p; // coefficient: p*turnRate = turnRateEff

	boolean completed;

        public Arc( Point c, Point source, Point dest ) {
            center = new Point(c);
            from   = new Point(source);
            to     = new Point(dest);

	    radius = center.distance(from);

	    adjustCenter();
	    calculateTurnPWM();
        }

	public String toString() {
	    return String.format("[Arc c%s from%s to%s; rC%s p(%.03f) angle(%.02f) rateEff(%.02f) [%.02f + %.02f] comp?(%b)]",
                       center,
		       from, to,
		       rCenter, p, turnAngle,
		       turnRateEff, turnA, turnB,
		       completed);
	}

	public void adjustCenter() {
	    double A = radius,
		   B = center.distance(to),
		   C = from.distance(to);

	    // This assumes we're not going to be choosing
	    //   points more than 180 degrees apart WRT to
	    //   the center of revolution.
	    double cos_c = (C*C - A*A - B*B)/(A*B); // Law of Cos
	    turnAngle = Math.toDegrees(Math.acos(cos_c));

	    if (Math.abs(A-B) < 0.01) {
		// center == rCenter
		rCenter = center;
	    } else {
		Line ab = new Line(from,to);
		Point ab_mid = from.midpoint(to);
		Line perp = ab.perpendicular(ab_mid);
		Point toward;
		// c_y will be compared to center.y to determine
		//   which direction rCenter will be.
		double c_y = ab.y(center.x);
		double sign = perp.m > 0  ? 1 : -1;
		if (center.y > c_y) {
		    toward = new Point(ab_mid.x+sign,
				       perp.y(ab_mid.x+sign));
		} else {
		    toward = new Point(ab_mid.x-sign,
				       perp.y(ab_mid.x+sign));
		}
		rCenter = 
		    ab_mid.distanceTowardPoint(toward,radius);
	    }
	}

	// R = (360*V)/(pi*p*(40-3V))

	public void calculateTurnPWM( ) {
	    calculateTurnPWM(Rules.MAX_VELOCITY);
	}
	public void calculateTurnPWM( double v ) {
	    p = 360*v/(Math.PI * radius * ( 40 - 3*v ));
	    turnA = Rules.getTurnRate(v);
	    turnRateEff = turnA * p;
	    turnB = (2*p-1)*turnA;
	}
	
    }
    Vector<Arc> movements;

    public CircularMovementModule( DataHeadRobot parent ) {
        super(parent);
        movements = new Vector<Arc>();

	listener();
    }

    public void listener() {
        parent.addCustomEvent(new Condition("TurnTick") {
                public boolean test() {
		    return true; // every turn (prob. ineffic.)
                }
            });
    }

    public void handleCustomEvent( CustomEvent e ) {
        if (e.getCondition().getName().equals("TurnTick")) {
	}
    }

    public void handleScannedRobotEvent( ScannedRobotEvent e ) {
	// update the target
    }

}
