package net.catchpole.B9.math;

import net.catchpole.B9.devices.gps.message.Location;

public class DistanceCalculator {
    public double degreesDistance(Location location, Location target) {
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
}
