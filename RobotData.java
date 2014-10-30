package jubjub_robofab;

import robocode.*;
import java.util.Vector;
import java.util.Date;

public class RobotData {
    public class RobotEvent {
        ScannedRobotEvent event;
        Point location;
        long time;
        DataHeadRobot parent;
	// instantaneous (self) values at the time of the scan
	double radarHeading, gunHeading, heading, 
	       gunHeat,
	       speed, x, y;

        public RobotEvent( ScannedRobotEvent e,
			   DataHeadRobot parent ) {
            this.parent = parent;
            event = e;
            this.time = parent.getTime();
            location = calculateLocation();
	    radarHeading = this.parent.getRadarHeading();
	    gunHeading   = this.parent.getGunHeading();
	    heading      = this.parent.getHeading();
	    gunHeat      = this.parent.getGunHeat();
	    speed        = this.parent.getVelocity();
	    x            = this.parent.getX();
	    y            = this.parent.getY();
        }

        // Calculate the position of the robot.
        public Point calculateLocation() {
            return Util.addVector(parent.getX(),parent.getY(),
                                  parent.getHeadingRadians()+event.getBearingRadians(),
                                  event.getDistance());
        }
    }

    Vector<RobotEvent> events;
    DataHeadRobot parent;
    String name;

    public RobotData( ScannedRobotEvent e, 
		      DataHeadRobot parent ) {
        this.parent = parent;
        events = new Vector<RobotEvent>();

        events.add(new RobotEvent(e,parent));

        name = e.getName();
    }

    public void add( ScannedRobotEvent e ) {
        events.add(new RobotEvent(e,parent));
    }

    public RobotEvent latest() {
        return events.lastElement();
    }
    public RobotEvent penultimate() {
        return events.elementAt(events.size()-2);
    }

    public Point getPosition() {
        return events.lastElement().location;
    }
    // Get the bearing relative to my current position.
    public double getBearing() {
        return events.lastElement().event.getBearing();
    }
    public double getBearingRadians() {
        return events.lastElement().event.getBearingRadians();
    }
    // Get the bearing relative to my current radar position.
    public double getRadarBearing() {
	return
	    Util.bearingToPoint(new Point(parent.getX(),
					  parent.getY()),
				parent.getRadarHeading(),
				events.lastElement().location);
    }

    public double energyDifference() {
	if (events.size() < 2) return 0;

        RobotEvent last = events.lastElement();
        RobotEvent prev = events.elementAt(events.size()-2);
        return prev.event.getEnergy() - last.event.getEnergy();
    }

    public String toString() {
        RobotEvent recent = events.lastElement();
        return String.format("[RobotData '%s' Pos(%.0f,%.0f) Head(%.2f) B(%.2f) V(%.1f) E(%.2f) Seen(%d)]\n",
                             name,
                             recent.location.x,
                             recent.location.y,
                             recent.event.getHeading(),
                             recent.event.getBearing(),
                             recent.event.getVelocity(),
                             recent.event.getEnergy(),
                             recent.time);
    }
}
