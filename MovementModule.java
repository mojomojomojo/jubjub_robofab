package jubjub_robofab;

import robocode.*;


public abstract class MovementModule {
    DataHeadRobot parent;

    protected MovementModule ( DataHeadRobot parent ) {
        this.parent = parent;
    }

    public abstract void listener();
    public abstract void handleCustomEvent( CustomEvent e );
    public abstract void handleScannedRobotEvent( ScannedRobotEvent e );

}
