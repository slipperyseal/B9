package net.catchpole.B9.math;

import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;

public class HeadingCalculator {
    private static final double EARTH_RADIUS = 6371.0d;
    private static final double MARS_RADIUS = 3390.0d;
    private static final double JUPITER_RADIUS = 69911.0d;

    public Heading getExpectedHeading(Location location, Location targetLocation) {
        double startLatitude = Math.toRadians(location.getLatitude());
        double startLongitude = Math.toRadians(location.getLongitude());
        double endLatitude = Math.toRadians(targetLocation.getLatitude());
        double endLongitude = Math.toRadians(targetLocation.getLongitude());
        double diffLogitude = endLongitude - startLongitude;
        double phi = Math.log(Math.tan(endLatitude/2.0+Math.PI/4.0)/Math.tan(startLatitude/2.0+Math.PI/4.0));
        if (Math.abs(diffLogitude) > Math.PI) {
            if (diffLogitude > 0.0) {
                diffLogitude = -(2.0 * Math.PI - diffLogitude);
            } else {
                diffLogitude = (2.0 * Math.PI + diffLogitude);
            }
        }

        double bearing = (Math.toDegrees(Math.atan2(diffLogitude, phi)) + 360.0) % 360.0;
        return new Heading(bearing / 360);
    }

    public Location getLocation(Location location, Heading heading, double distance) {
        double dr = distance / EARTH_RADIUS;
        double bearing = Math.toRadians(heading.getDegrees() * 360.0d);

        double locationLatitude = Math.toRadians(location.getLatitude());
        double locationLogitude = Math.toRadians(location.getLongitude());

        double latitude = Math.asin( Math.sin(locationLatitude) * Math.cos(dr) +
                Math.cos(locationLatitude) * Math.sin(dr) * Math.cos(bearing) );
        double longitude = locationLogitude + Math.atan2(
                Math.sin(bearing) * Math.sin(dr) * Math.cos(locationLatitude),
                Math.cos(dr) - Math.sin(locationLatitude) * Math.sin(latitude));
        return new Location(Math.toDegrees(latitude), Math.toDegrees(longitude));
    }

    public double getSwivel(Heading heading, Heading newHeading) {
        double swivel = newHeading.getDegrees() - heading.getDegrees();
        if (swivel > 1.0d) {
            swivel -= 1.0d;
        }
        if (swivel > 0.5d) {
            swivel = 0.0d - (1.0d - swivel);
        }
        return swivel;
    }
}
