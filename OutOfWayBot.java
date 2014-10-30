package jubjub_robofab;

import robocode.*;

import java.io.PrintStream;


public class OutOfWayBot extends AdvancedRobot
{
    public void goTo( double x, double y, double heading ) {
        //out.printf("Moving to:   (%.01f,%.01f)\n",x,y);
        //out.printf("My Location: (%.01f,%.01f)\n",getX(),getY());
        //out.printf(" My Heading: %.02f\n",getHeading());

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
        // Go to the top center of the map.
        goTo(20,20,0);
        
        turnRadarRight(Double.POSITIVE_INFINITY);
        while (true) {
            execute();
        }
    }
}
