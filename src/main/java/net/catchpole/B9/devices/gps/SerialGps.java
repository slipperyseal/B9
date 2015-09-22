package net.catchpole.B9.devices.gps;

import net.catchpole.B9.devices.compass.Compass;
import net.catchpole.B9.devices.gps.listener.LocationListener;
import net.catchpole.B9.devices.gps.listener.MessageListener;
import net.catchpole.B9.devices.gps.listener.VectorListener;
import net.catchpole.B9.devices.serial.PiCommPort;
import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;
import net.catchpole.B9.spacial.Vector;

import java.io.IOException;

public class SerialGps implements Gps, Compass {
    private final PiCommPort piCommPort;
    private final GpsParser gpsParser = new GpsParser();
    private volatile Vector vector;
    private volatile Location location;

    public SerialGps() throws IOException {
        gpsParser.addListener(new VectorListener() {
            public void listen(Vector vector) {
                SerialGps.this.vector = vector;
            }
        });

        gpsParser.addListener(new LocationListener() {
            public void listen(Location location) {
                SerialGps.this.location = location;
            }
        });

        this.piCommPort = new PiCommPort() {
            @Override
            public void process(String line) {
                processLine(line);
            }
        };
    }

    public void addListener(MessageListener messageListener) {
        this.gpsParser.addListener(messageListener);
    }

    public Location getLocation() {
        while (location == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
        return location;
    }

    public Heading getHeading() {
        while (vector == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
        return vector.getHeading();
    }

    public void processLine(String line) {
        this.gpsParser.parse(line);
    }
}
