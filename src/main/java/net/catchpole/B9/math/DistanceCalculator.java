package net.catchpole.B9.math;

import net.catchpole.B9.spacial.Location;

public class DistanceCalculator {
    public double degreesDistance(Location location, Location target) {
        target = getWrappedLocation(location, target);
        return Math.sqrt(Math.pow((location.getLatitude() - target.getLatitude()), 2.0) +
                Math.pow((location.getLongitude() - target.getLongitude()), 2.0));
    }

    public boolean isWithinDistance(Location location, Location target, double meters) {
        return distanceMeters(location, target) <= meters;
    }

    public double metersToDegreesLatitude(double meters, Location location) {
        return 1.0D / (distanceMeters(location, new Location(location.getLatitude()+1.0D, location.getLongitude())) / meters);
    }

    public double metersToDegreesLongitude(double meters, Location location) {
        return 1.0D / (distanceMeters(location, new Location(location.getLatitude(), location.getLongitude() + 1.0D)) / meters);
    }

    public double distanceMeters(Location location1, Location location2) {
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
        double distanceMeters = HeadingCalculator.EARTH_RADIUS * c * 1000;
        double height = elevation1 - elevation2;
        return Math.sqrt(Math.pow(distanceMeters, 2) + Math.pow(height, 2));
    }

    public Location getWrappedLocation(Location location, Location target) {
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
}
