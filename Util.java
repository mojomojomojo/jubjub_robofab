package jubjub_robofab;

public class Util {

    public static Point addVector( double x, double y,
                                   double vec_rad, double mag ) {

	// System.out.println(String.format("(%.0f,%.0f) + H(%.2f) M(%.2f) => (%.0f,%.0f)",
	// 				 x,y, vec_rad, mag,
	// 				 x + mag * Math.sin(vec_rad),
	// 				 y + mag * Math.cos(vec_rad)));

        return new Point( x + mag * Math.sin(vec_rad),
                          y + mag * Math.cos(vec_rad) );
    }

    public static double norm( double angle ) {
	while (angle > 180) {
	    angle -= 360;
	}
	while (angle < -180) {
	    angle += 360;
	}
	return angle;
    }

    public static double bearingToPoint( Point me, double heading,
					 Point target ) {
	return
	    norm(
		 Math.toDegrees(Math.atan2(target.x-me.x,
					   target.y-me.y))
		 - heading
		 );
    }
}
