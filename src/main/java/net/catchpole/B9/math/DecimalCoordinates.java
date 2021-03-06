package net.catchpole.B9.math;

import net.catchpole.B9.spacial.Location;

public class DecimalCoordinates {
    private DecimalCoordinates() {
    }

    public static Location fromDegreesMinutes(String latitude, String northSouth, String longitude, String westEast) {
        return new Location(latitudeFromDegreesMinutes(latitude, northSouth),
                longitudeFromDegreesMinutes(longitude, westEast));
    }

    private static double toDecimal(String value) {
        int dot = value.indexOf('.');
        double degrees = Double.parseDouble(value.substring(0, dot - 2));
        double seconds = Double.parseDouble(value.substring(dot - 2));
        return degrees+(seconds/60);
    }

    public static double latitudeFromDegreesMinutes(String latitude, String northSouth) {
        double lat = toDecimal(latitude);
        return "N".equalsIgnoreCase(northSouth) ? lat : 0.0D-lat;
    }

    public static double longitudeFromDegreesMinutes(String longitude, String westEast) {
        double lon = toDecimal(longitude);
        return "E".equalsIgnoreCase(westEast) ? lon : 0.0D-lon;
    }
}
