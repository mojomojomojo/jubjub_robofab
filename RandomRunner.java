package jubjub_robofab;
import robocode.*;
import java.awt.Color;

import java.util.Random;
import java.util.Vector;
import java.util.Iterator;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * RandomRunner - a robot by (your name here)
 */
public class RandomRunner extends Robot
{
    Vector<Bullet> bullets;

    /**
     * run: RandomRunner's default behavior
     */
    public void run() {
	bullets = new Vector<Bullet>();
	setScanColor(Color.red);
	
        // After trying out your robot, try uncommenting the import at the top,
        // and the next line:

        // setColors(Color.red,Color.blue,Color.green); // body,gun,radar

        // Robot main loop
  	Random turnOrNot = new Random();
        Random turnLR = new Random();
	Random turnAmount = new Random();
	Random moveDir = new Random();
	Random moveAmount = new Random();
	Random gunLR = new Random();
	Random gunAmount = new Random();
	Random fireYN = new Random();
	Random firePower = new Random();
        while(true) {
	    // Decide if we should turn or keep the same heading.
	    if (turnOrNot.nextInt(4) < 2) {
		double amount = turnAmount.nextDouble()*90;
		if (turnLR.nextBoolean()) {
		    // Turn left.
		    out.println("Left: "+amount);
		    turnLeft(amount);
		} else {
		    // Turn right.
		    out.println("Right: "+amount);
		    turnRight(amount);
		}
	    }
	    // Decide how much to move.
	    int amount = moveAmount.nextInt(200);
	    if (moveDir.nextBoolean()) {
		out.println("Ahead: "+amount);
		ahead(amount);
	    } else {
		out.println("Back: "+amount);
		back(amount);
	    }

	    // Decide how much to move the gun.
	    if (gunLR.nextInt(4) < 2) {
		double g_amount = gunAmount.nextDouble()*90;
		if (gunLR.nextBoolean()) {
		    out.println("Gun Left: "+g_amount);
		    turnGunLeft(g_amount);
		} else {
		    out.println("Gun Right: "+g_amount);
		    turnGunRight(g_amount);
		}
	    }

	    // Decide whether or not to fire.
	    if (fireYN.nextBoolean()) {
		double power = firePower.nextDouble()*(Rules.MAX_BULLET_POWER-Rules.MIN_BULLET_POWER)+Rules.MIN_BULLET_POWER;
		out.println("Fire: "+power);
		Bullet b = fireBullet(power);
		out.println("Fire: "+b);
		bullets.add(b);
	    }

	    // Print all the bullets.
	    for (Iterator<Bullet> i=bullets.iterator(); i.hasNext(); ) {
		Bullet b = i.next();
		if (b==null) continue;
		out.println(String.format("[BULLET] (%.0f,%.0f) V(%.2f) H(%.1f) P(%.1f)",
					  b.getX(),b.getY(),
					  b.getVelocity(),
					  b.getHeading(),
					  b.getPower()));
	    }
        }
    }

    public void safeAhead( double distance ) {
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent e) {
    }
    
    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
    }    
}
