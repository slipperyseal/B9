package net.catchpole.B9.math;

import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;

public class Geo {
    public static final double EARTH_RADIUS = 6371.0d;
    public static final double MARS_RADIUS = 3390.0d;
    public static final double JUPITER_RADIUS = 69911.0d;

    private Geo() {
    }

    public static double degreesDistance(Location location, Location target) {
        target = getWrappedLocation(location, target);
        return Math.sqrt(Math.pow((location.getLatitude() - target.getLatitude()), 2.0) +
                Math.pow((location.getLongitude() - target.getLongitude()), 2.0));
    }

    public static boolean isWithinDistance(Location location, Location target, double meters) {
        return distanceMeters(location, target) <= meters;
    }

    public static double metersToDegreesLatitude(double meters, Location location) {
        return 1.0D / (distanceMeters(location, new Location(location.getLatitude()+1.0D, location.getLongitude())) / meters);
    }

    public static double metersToDegreesLongitude(double meters, Location location) {
        return 1.0D / (distanceMeters(location, new Location(location.getLatitude(), location.getLongitude() + 1.0D)) / meters);
    }

    public static double distanceMeters(Location location1, Location location2) {
        double latitude1 = location1.getLatitude();
        double latitude2 = location2.getLatitude();
        double longitude1 = location1.getLongitude();
        double longitude2 = location2.getLongitude();
        double elevation1 = location1.getAltitude();
        double elevation2 = location2.getAltitude();

        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceMeters = EARTH_RADIUS * c * 1000;
        double height = elevation1 - elevation2;
        return Math.sqrt(Math.pow(distanceMeters, 2) + Math.pow(height, 2));
    }

    public static Location getWrappedLocation(Location location, Location target) {
        boolean lat  = (location.getLatitude() - target.getLatitude()) > 180.0d || (location.getLatitude() - target.getLatitude()) < -180.d;
        boolean lon  = (location.getLongitude() - target.getLongitude()) > 180.0d || (location.getLongitude() - target.getLongitude()) < -180.0d;
        if (lat || lon) {
            return new Location(
                    lat ? target.getLatitude() - 360.0d : target.getLatitude(),
                    lon ? target.getLongitude() - 360.0d : target.getLongitude(),
                    target.getAltitude());
        }
        return target;
    }

    public static Heading getExpectedHeading(Location location, Location targetLocation) {
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

        return new Heading((Math.toDegrees(Math.atan2(diffLogitude, phi)) + 360.0) % 360.0);
    }

    public static Location getLocation(Location location, Heading heading, double meters) {
        double dr = meters / (EARTH_RADIUS*1000.0D);
        double bearing = Math.toRadians(heading.getDegrees());

        double locationLatitude = Math.toRadians(location.getLatitude());
        double locationLogitude = Math.toRadians(location.getLongitude());

        double latitude = Math.asin( Math.sin(locationLatitude) * Math.cos(dr) +
                Math.cos(locationLatitude) * Math.sin(dr) * Math.cos(bearing) );
        double longitude = locationLogitude + Math.atan2(
                Math.sin(bearing) * Math.sin(dr) * Math.cos(locationLatitude),
                Math.cos(dr) - Math.sin(locationLatitude) * Math.sin(latitude));
        return new Location(Math.toDegrees(latitude), Math.toDegrees(longitude));
    }

    public static double getSwivel(Heading heading, Heading newHeading) {
        double swivel = toDecimalFraction(newHeading.getDegrees() - heading.getDegrees());
        if (swivel > 1.0d) {
            swivel -= 1.0d;
        }
        if (swivel > 0.5d) {
            swivel = 0.0d - (1.0d - swivel);
        }
        return swivel;
    }

    public static double toDecimalFraction(Heading heading) {
        return heading.getDegrees() / 360.0d;
    }

    public static double toDecimalFraction(double degrees) {
        return degrees / 360.0d;
    }
}
