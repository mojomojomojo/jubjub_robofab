package jubjub_robofab;

import robocode.*;
import java.awt.Color;
import java.awt.Graphics2D;

import java.util.Random;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;

import robocode.util.Utils;
import robocode.CustomEvent;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html


/**
 * RandomRunner - a robot by (your name here)
 */
public class Tower extends DataHeadRobot
{
    // Name => RobotData
    Hashtable<String,RobotData> enemies;
    Hashtable<String,RobotData> allies;

    public enum State {
        INITIAL_SCAN,
        NORMAL
    }
    State state;
    // The most recent radar sweep measurement.

    BulletData enemyBullets;

    long timerStart;

    // Radar Lock
    RadarLock radarLock;
    // Movement Style(s)
    MovementModule movement;

    public void run() {
        // Rules
        out.printf("Acc/Dec: %.2f/%.2f\nTurn Rates:\n  Tank: %.2f\n  Gun: %.2f\n  Radar: %.2f\nBullets:\n  Power: [%.2f,%.2f]\nCollisions:\n  Bonus: %.2f\n  Damage: %.2f\n",
                   Rules.ACCELERATION,
                   Rules.DECELERATION,
                   Rules.MAX_TURN_RATE,
                   Rules.GUN_TURN_RATE,
                   Rules.RADAR_TURN_RATE,
                   Rules.MIN_BULLET_POWER, Rules.MAX_BULLET_POWER,
                   Rules.ROBOT_HIT_BONUS,
                   Rules.ROBOT_HIT_DAMAGE
                   );

        enemies = new Hashtable<String,RobotData>();
        allies = new Hashtable<String,RobotData>();
        enemyBullets = new BulletData(this);
	radarLock = new RadarLock(this);
	movement = new CircularMovementModule(this);

	setScanColor(Color.white);
        setColors(Color.black,Color.black,Color.white); // body,gun,radar

	// Quick Sweep
	// radar 45deg / turn
	// gun 20deg / turn
	// tank 10deg / turn
        state = State.INITIAL_SCAN;
	double fsTurns = 360/75.0;
	setTurnRadarRight(fsTurns*45);
	setTurnGunRight(fsTurns*20);
	setTurnRight(fsTurns*10);
	for (int i=0; i<5; i++) {
	    execute();
	}
        out.println("Finished initial scan.\n");



	setAdjustRadarForRobotTurn(true);
	setAdjustRadarForGunTurn(true);
	setAdjustGunForRobotTurn(true);

        // Check for bullet validity (off-screen) every 20 turns.
        addCustomEvent(new Condition("EnemyBulletCheck") {
                public boolean test() {
                    return (getTime() - timerStart >= 20);
                }
            });

	state = State.NORMAL;
        if (target == null) {
            chooseTarget();
        }
	radarLock.seekTarget(target);
        while(true) {
            execute();
        }
    }

    public void chooseTarget() {
	out.println("[SELECTING_TARGET]");
        if (getOthers() == 1) {
            target = enemies.get(enemies.keys().nextElement());
        } else {
            Random prng = new Random();
            int choice = prng.nextInt(enemies.size());
            Enumeration<String> e = enemies.keys();
            for (int i=0; i<=choice; i++) {
                if (i==choice) {
                    target = enemies.get(e.nextElement());
                }
            }
        }
        out.println(String.format("[TARGET_ACQUIRED] %s",target.name));
    }

    public void onCustomEvent(CustomEvent e) {
        // Mark enemy bullets inactive that are not a threat.
        if (e.getCondition().getName().equals("EnemyBulletCheck")) {
	    enemyBullets.checkActive();
            //out.println(enemyBullets);
            timerStart = getTime();

	}

	// Give the submodules a chance to examine the event.
	radarLock.customEvent(e);
	movement.handleCustomEvent(e);
    }


    public void onPaint(Graphics2D g) {
	enemyBullets.checkActive();
        for (Iterator b = enemyBullets.iterator(); b.hasNext(); ) {
            BulletData.BulletEvent be = (BulletData.BulletEvent)b.next();
            int r = (int)(be.speed * (getTime()-be.fireTime));
            //out.println(String.format("wave: %s -> %d",be.origin,r));
	    be.drawCloud(g);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Assume they're all enemies.
        RobotData robot = enemies.get(e.getName());
        if (robot == null) {
            enemies.put(e.getName(),new RobotData(e,this));
            robot = enemies.get(e.getName());
        } else {
            robot.add(e);
        }
	//dumpSR(e);
        //out.println(robot);

	out.println(String.format("[SCAN_ROBOT] %s %s: (rem:%.1f)",
				  robot.name,
				  robot.latest().location,
				  getRadarTurnRemaining()));


        double eDiff = robot.energyDifference();
        if (eDiff > 0) {
            out.println(String.format("Energy Drop: %.1f",eDiff));
            enemyBullets.event(robot);
        }

	// If there's only one opponent, start the lock now.
	if (getOthers()==1) {
	    setAdjustRadarForRobotTurn(true);
	    setAdjustRadarForGunTurn(true);
	    setAdjustGunForRobotTurn(true);

	    state = State.NORMAL;
	    if (target == null) {
		chooseTarget();
	    }
	    radarLock.seekTarget(target);
	}
	radarLock.scannedRobot(e);
	movement.handleScannedRobotEvent(e);
    }

    public void dumpSR( ScannedRobotEvent e ) {
	out.printf("[%s] H(%.1f)/B(%.1f) V(%.1f) E(%.1f) D(%.1f)\n",
		   e.getName(),
		   e.getHeading(),
		   e.getBearing(),
		   e.getVelocity(),
		   e.getEnergy(),
		   e.getDistance());
    }
}
