package net.catchpole.B9.math;

import net.catchpole.B9.spacial.Location;

public class DistanceCalculator {
    public double degreesDistance(Location location, Location target) {
        target = getWrappedLocation(location, target);
        return Math.sqrt(Math.pow((location.getLatitude() - target.getLatitude()), 2.0) +
                Math.pow((location.getLongitude() - target.getLongitude()), 2.0));
    }

    public boolean isWithinDistance(Location location, Location target, double kilometers) {
        return degreesToKilometers( degreesDistance(location, target) ) <= kilometers;
    }

    public double kilometersToDegrees(double kilometers) {
        return kilometers / 111.0d;
    }

    public double degreesToKilometers(double degrees) {
        return degrees * 111.0d;
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
