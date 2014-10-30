package jubjub_robofab;

import robocode.*;

import java.util.Vector;


/*
 * RadarLock
 *   This class is designed to implement radar locking.
 *   Since radar operates more or less independently of the
 *     other robot functions, it could be separated out.
 */
public class RadarLock {
    public enum SweepDir {
        RIGHT,
        LEFT
    }

    public enum SweepPhase {
        SWEEP,
        FASTSWEEP,
        SEEK,
        FORWARD,
        BACK
    }

    DataHeadRobot parent;
    RobotData target;
    Vector<Double> sweeps;
    SweepPhase phase;

    public RadarLock( DataHeadRobot parent ) {
        this.parent = parent;
        listener();
        sweeps = new Vector<Double>();
        sweeps.add(0.0);
    }

    public java.io.PrintStream debug( String fmt, Object... args ) {
        parent.out.print(String.format("%05d ",parent.getTime())+
                         String.format(fmt,args));
        return parent.out;
    }

    public void listener() {
        parent.addCustomEvent(new Condition("radarMoveFinished") {
                public boolean test() {
                    return parent.getRadarTurnRemaining() == 0;
                }
            });
    }

    public void customEvent( CustomEvent e ) {
        if (e.getCondition().getName().equals("radarMoveFinished")) {
            debug("[RADAR_MOVE_FINISHED] rdrH(%.1f) prev(%.1f)\n",
                  parent.getRadarHeading(),
                  sweeps.lastElement());

            if (phase == SweepPhase.SEEK ||
                phase == SweepPhase.BACK) {
                sweepForward();
            } else if (phase == SweepPhase.FORWARD) {
                sweepBack();
            }
        }
    }

    public void scannedRobot( ScannedRobotEvent e ) {
        debug("[RADAR_SCANNED_ROBOT] rdrH(%.1f) rem(%.1f) prev(%.1f)\n",
              parent.getRadarHeading(),
              parent.getRadarTurnRemaining(),
              sweeps.lastElement() );

        if (phase == SweepPhase.SEEK ||
            phase == SweepPhase.BACK) {
            sweepForward();
        } else if (phase == SweepPhase.FORWARD) {
            sweepBack();
        }
    }

    public void seekTarget( RobotData target ) {
        this.target = target;

        // As soon as the robot is scanned (again), change
        //   from seek to forward.
        double b = target.getRadarBearing();
        int sign = b>0?1:-1;
        double adj_radar =
            Util.norm(Math.ceil(Math.abs(b)/45)*45*sign);
        debug("[SEEK] %.1f (%.1f -> %.1f)\n",
              adj_radar,
              parent.getRadarHeading(),
              target.getRadarBearing());

        phase = SweepPhase.SEEK;
        parent.setTurnRadarRight(adj_radar); 
    }
    
    public void radarMoved() {
    }

    public void radarSeek() {
    }
    public void radarForward() {
    }

    public void sweepForward() {
        // sweep "forward"
        double adjust = target.getRadarBearing();
        adjust += 15;
        phase = SweepPhase.FORWARD;
        parent.setTurnRadarRight(adjust);
        debug("[FORWARD] %.1f (%.1f -> %.1f)\n",
              adjust,
              parent.getRadarHeading(),
              target.getRadarBearing());
    }

    public void sweepBack() {
        // sweep "back"
        double adjust = target.getRadarBearing();
        adjust -= 15;
        phase = SweepPhase.BACK;
        parent.setTurnRadarRight(adjust);
        debug("[FORWARD] %.1f (%.1f -> %.1f)\n",
              adjust,
              parent.getRadarHeading(),
              target.getRadarBearing());
    }


}
