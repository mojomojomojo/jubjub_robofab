package jubjub_robofab;

import robocode.*;

import java.io.PrintStream;
import java.util.Vector;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;


public class TurnTester extends AdvancedRobot
{
    Vector<Point> loci;
    Point center;
    double radius;

    

    public java.io.PrintStream openDataFile( String filename ) {
	try {
	    return
		new PrintStream(
		    new RobocodeFileOutputStream(
		        getDataFile(
                            filename
		                   )
		    )
	        );
	} catch (Exception e) {
	    out.println("Error opening data output stream: "+e);
	}
	return null;
    }

    public void goTo( double x, double y, double heading ) {
        out.printf("Moving to:   (%.01f,%.01f)\n",x,y);
        //out.printf("My Location: (%.01f,%.01f)\n",getX(),getY());
        //out.printf(" My Heading: %.02f\n",getHeading());

	setMaxVelocity(Rules.MAX_VELOCITY);
	setMaxTurnRate(Rules.MAX_TURN_RATE);
        double dy = y-getY(),
               dx = x-getX();
        double targetHead = 90-Math.toDegrees(Math.atan2(dy,dx));
        //out.printf("   target heading: %.02f\n",targetHead);
        double bearing = targetHead-getHeading();
        //out.printf("          bearing: %.02f\n",bearing);
        turnRight(bearing);
        double d = Math.sqrt(dy*dy + dx*dx);
        //out.printf("   distance: %.02f\n",d);
        ahead(d);
        //out.printf(" my heading: %.02f\n",getHeading());
        turnRight(90-getHeading());
    }

    public void run() {
        out.println(getDataDirectory());

        double tc_x = getBattleFieldWidth()/2;
        double tc_y = getBattleFieldHeight()*.9;
        double startHeading = 90;

	for (double max_v=Rules.MAX_VELOCITY; max_v>=2; max_v-=.1) {

	    for (double effRate=.5; effRate <= (10-3*max_v/4)+.1; effRate += .1) {
            //double turnA = 4*effRate/2; // 4
            //double turnB = turnA; // (effRate*2-turnA);
            int turn = 0;

            // Go to the top center of the map.
            goTo(tc_x,tc_y,90);
            turnGunRight(1080); // decrease our average CPU use

	    setMaxVelocity(max_v);
	    setMaxTurnRate(effRate);
            out.printf("%.01f/%.01f\n",effRate,max_v);

	    String filename =
		String.format("position_%.02f_v%.1f.csv",
			      effRate,max_v);
	    PrintStream data_out = openDataFile(filename);

            loci = new Vector<Point>();
            int lapsToGo = 4;
            double prevHead = 91;
            long lastLap = 0;
            while (true) {
                loci.add(new Point(getX(),getY()));
                analysis(false);
                if (turn % 2 == 0) {
                    if (prevHead <= 90.01 && getHeading() > 90 && 
                        Math.abs(prevHead-getHeading()) < 10 &&
                        getTime()-lastLap > 10) {

                        out.printf("   Laps remaining: %d\n",lapsToGo);
                        lastLap = getTime();
                        //setFire(.1);
                        if (--lapsToGo == 0) {
                            // next effRate
                            break;
                        }
                    }
                    prevHead = getHeading();
                }

                if (getVelocity() == max_v) {
                    data_out.printf("%05d,%.2f,%.2f,%.2f\n",
                                    getTime(),
                                    getHeading(),
                                    getX(),getY());
                }

                // if (turn++ % 2 == 0) {
                //     setTurnRight(turnA);
                //     //out.print("A");
                // } else {
                //     setTurnRight(turnB);
                //     //out.print("B");
                // }
                setAhead(100000);
                setTurnRight(360);
                execute();
            }
	    setAhead(0);
	    setTurnRight(0);

            analysis();
            data_out.close();

	    // handshake file
	    // data_out = openDataFile(filename+".done");
	    // data_out.print("1");
	    // data_out.close();
        }
	}
	while (true) {
	    // kill self
	    fire(Rules.MAX_BULLET_POWER);
	}
    }


    public void onPaint(Graphics2D g) {
        if (loci == null) return;

        g.setColor(Color.red);
        for (Point p : loci) {
            Circle p_c = new Circle(p,2);
            p_c.draw(g);
        }
        if (center != null) {
            Circle c = new Circle(center,4);
            g.setColor(Color.white);
            c.draw(g);
        }
    }

    public void analysis() { analysis(true); }

    public void analysis( boolean display ) {
        // Attempt to calculate the center of revolution.
        double avg_x = 0, avg_y = 0,
               min_x = Double.POSITIVE_INFINITY,
               max_x = Double.NEGATIVE_INFINITY,
               min_y = Double.POSITIVE_INFINITY,
               max_y = Double.NEGATIVE_INFINITY;
        for ( Point p : loci ) {
            avg_x += p.x;
            avg_y += p.y;
            min_x = Math.min(min_x,p.x);
            max_x = Math.max(max_x,p.x);
            min_y = Math.min(min_y,p.y);
            max_y = Math.max(max_y,p.y);
        }
        avg_x /= loci.size();
        avg_y /= loci.size();
        center = new Point(avg_x,avg_y);
        double avg_d = 0;
        for ( Point p : loci ) {
            avg_d += center.distance(p);
        }
        avg_d /= loci.size();
        radius = avg_d;
        if (display)
            out.printf("Center: %s, Radius: %.02f\n",center,radius);
    }
}
