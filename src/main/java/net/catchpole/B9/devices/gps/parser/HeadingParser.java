package net.catchpole.B9.devices.gps.parser;

import net.catchpole.B9.devices.compass.Heading;

public class HeadingParser implements GpsMessageParser<Heading> {
    public Heading parse(String[] line) {
        double value = 1.0D/360.D * Double.parseDouble(line[1]);
        if (value >= 1.0D) {
            value = Math.floor(value);
        }
        return new Heading(value);
    }

    public String getKey() {
        return "$GPVTG";
    }
}
