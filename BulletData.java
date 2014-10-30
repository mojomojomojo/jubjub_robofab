package jubjub_robofab;
import robocode.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.util.Vector;
import java.util.Iterator;

public class BulletData {
    DataHeadRobot parent;
    Vector<BulletEvent> bullets;
    Point[] corners;

    public class BulletEvent {
	boolean active;
        double power;
        RobotData firer;
        double speed;
	// There's some guaranteed uncertainty in this.
        Point originA, originB, origin;
        long fireTimeA, fireTimeB, fireTime;

	// Attempt to make guesses about the most dangerous
	//   places.
	DataHeadRobot parent;
	Point still, linearA, linearB;

        public BulletEvent( RobotData robot,
			    DataHeadRobot parent ) {
            RobotData.RobotEvent preShot = robot.penultimate();
	    RobotData.RobotEvent postShot = robot.latest();

	    this.parent = parent;
            firer = robot;
            power = robot.energyDifference();
            speed = 20 - 3*power;
            fireTimeA = preShot.time;
            fireTimeB = postShot.time;
            originA = preShot.location;
            originB = postShot.location;
	    // Assume the actual firing was closer to the last
	    //   ScannedRobotEvent.
	    fireTime = fireTimeA + (fireTimeB-fireTimeA)/4;
	    origin = originA.midpoint(originB,.25);
	    active = true;

	    still = new Point(this.parent.getX(),
			      this.parent.getY());
        }

        public String toString() {
            return String.format("[BulletEvent %s Origin%s P(%.1f) S(%.1f) T(%d)]",
				 active?"ACT":"!in",
                                 origin, power, speed, fireTime);
        }

	public double distance() {
	    return speed * (parent.getTime()-fireTime);
	}

	public void drawCloud( Graphics2D g ) {
	    // System.out.println(String.format("%s (%s)",
	    // 			      active?"ACTIVE":"inactive",
	    // 			      this));
	    if (! active) return; // nothing to do

	    double r = distance();
	    // draw the fire point
            g.setColor(Color.red);
	    Circle oA = new Circle(originA,2),
		   oB = new Circle(originB,2),
		   o_ = new Circle(origin,3);
	    oA.draw(g);
	    oB.draw(g);
	    g.setStroke(new BasicStroke(1f));
	    oA.center.drawLine(g,oB.center);
            g.setColor(Color.blue);
	    o_.fill(g);

	    Circle eHA = new Circle(originA,r),
		   eHB = new Circle(originB,r),
		   eH_ = new Circle(origin,r);
            g.setColor(Color.blue);
	    g.setStroke(new BasicStroke(3.0f));
	    eH_.draw(g);
	    g.setColor(Color.white);
	    g.setStroke(new BasicStroke(.5f));
	    eHA.draw(g);
	    eHB.draw(g);
	}
    }

    // Verify that each of the bullets is possibly active
    //   (i.e. could still be on the board).
    public void checkActive() {
	bullet:
	for (Iterator<BulletEvent> i=bullets.iterator(); i.hasNext(); ) {
	    BulletEvent b = i.next();
	    if (! b.active) continue; // Don't check it again.

	    double traveled = b.distance();
	    for (int c_i=0; c_i<4; c_i++) {
		// System.out.println(String.format("[CHECK_ACTIVE] %s => Corner %s: D(%.1fn) <=?> %.1f",
		// 				 b.origin,
		// 				 corners[c_i],
		// 				 b.origin.distance(corners[c_i]),
		// 				 traveled));
		if (b.origin.distance(corners[c_i]) > traveled) {
		    continue bullet;
		}
	    }
	    //System.out.println("Bullet Inactive: "+b);
	    b.active = false;
	}
    }

    public Iterator iterator() {
        return bullets.iterator();
    }

    public BulletData( DataHeadRobot parent ) {
        this.parent = parent;
        bullets = new Vector<BulletEvent>();
	corners = new Point[4];
	// LL
	corners[0] = new Point(0,0);
	// UL
	corners[1] = new Point(0,parent.getBattleFieldHeight());
	// UR
	corners[2] = new Point(parent.getBattleFieldWidth(),
			       parent.getBattleFieldHeight());
	// LR
	corners[3] = new Point(parent.getBattleFieldWidth(),0);
    }

    public void event( RobotData robot ) {
        bullets.add(new BulletEvent(robot,parent));
        //parent.out.println(bullets.lastElement());
    }

    public String toString() {
        StringBuilder str = new StringBuilder("  ");
        for (Iterator<BulletEvent> i = bullets.iterator(); i.hasNext(); ) {
	    BulletEvent b = i.next();
	    if (! b.active) continue; // skip this one
            str.append("  " + b + "\n");
        }
        return str.toString();
    }
}
