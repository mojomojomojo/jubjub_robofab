package jubjub_robofab;

import robocode.*;

import java.util.Hashtable;

/**
 * DataHeadRobot - an AdvancedRobot with more features.
 */
public abstract class DataHeadRobot extends AdvancedRobot {
    // Name => RobotData
    Hashtable<String,RobotData> enemies;
    Hashtable<String,RobotData> allies;

    BulletData enemyBullets;

    RobotData target;
}
