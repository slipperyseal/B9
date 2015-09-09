package net.catchpole.B9.devices.hardware;

import net.catchpole.B9.devices.compass.Compass;
import net.catchpole.B9.devices.gps.Gps;
import net.catchpole.B9.devices.gps.GpsParser;
import net.catchpole.B9.devices.gps.listener.HeadingListener;
import net.catchpole.B9.devices.gps.listener.LocationListener;
import net.catchpole.B9.devices.gps.listener.MessageListener;
import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;

import java.io.IOException;

public class SerialGps implements Gps, Compass {
    private final PiCommPort piCommPort;
    private final GpsParser gpsParser = new GpsParser();
    private volatile Heading heading;
    private volatile Location location;

    public SerialGps() throws IOException {
        gpsParser.addListener(new HeadingListener() {
            public void listen(Heading heading) {
                SerialGps.this.heading = heading;
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
        while (heading == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
        return heading;
    }

    public void processLine(String line) {
        this.gpsParser.parse(line);
    }
}
