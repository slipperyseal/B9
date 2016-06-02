package net.catchpole.B9.devices.gps.parser;

import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Vector;

public class VectorParser implements GpsMessageParser<Vector> {
    public Vector parse(String[] line) {
        return new Vector(new Heading(Double.parseDouble(line[1])), Double.parseDouble(line[7]));
    }

    public String getKey() {
        return "$GPVTG";
    }
}
