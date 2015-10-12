package net.catchpole.B9.devices.gps.parser;

import net.catchpole.B9.devices.gps.message.RecommendedMinimum;
import net.catchpole.B9.math.DecimalCoordinates;

public class RecommendedMinimumParser implements GpsMessageParser<RecommendedMinimum> {
    private DecimalCoordinates decimalCoordinates = new DecimalCoordinates();

    public RecommendedMinimum parse(String[] line) {
        if (line[2].length() == 1) {
            RecommendedMinimum recommendedMinimum = new RecommendedMinimum();
            recommendedMinimum.setStatus(parseCode(line[2]));
            String hours = line[1];
            String days = line[9];
            if (hours != null && hours.length() >= 6 && days != null && days.length() == 6) {
                recommendedMinimum.setTime(
                        hours.substring(0,2) + ':' +
                                hours.substring(2,4) + ':' +
                                hours.substring(4,6) + ':' +
                                ((hours.length() > 6 && hours.charAt(6) == '.') ? hours.substring(7) : "000") +
                                ' ' +
                                days.substring(0,2) + ':' +
                                days.substring(2,4) + ':' +
                                days.substring(4,6));
            }
            recommendedMinimum.setSpeedOverGroundKnots(parseDouble(line[7]));
            recommendedMinimum.setTrackAngle(parseDouble(line[8]));
            if (!line[10].isEmpty() && !line[11].isEmpty()) {
                recommendedMinimum.setMagneticVariation(decimalCoordinates.longitudeFromDegreesMinutes(line[10], line[11]));
            }
            return recommendedMinimum;
        }
        return null;
    }

    public String getKey() {
        return "$GPRMC";
    }

    private Double parseDouble(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(string);
        } catch (Throwable t) {
            return null;
        }
    }

    private char parseCode(String string) {
        return string == null || string.length() != 1 ? 0 : string.charAt(0);
    }
}
