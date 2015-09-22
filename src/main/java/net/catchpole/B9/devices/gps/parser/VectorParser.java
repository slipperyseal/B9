package net.catchpole.B9.devices.gps.parser;

import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Vector;

public class VectorParser implements GpsMessageParser<Vector> {
    public Vector parse(String[] line) {
        double value = 1.0D/360.D * Double.parseDouble(line[1]);
        if (value >= 1.0D) {
            value = Math.floor(value);
        }
        return new Vector(new Heading(value), Double.parseDouble(line[7]));
    }

    public String getKey() {
        return "$GPVTG";
    }
}
