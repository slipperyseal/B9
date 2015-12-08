package net.catchpole.B9.devices.gps;

import net.catchpole.B9.devices.compass.Compass;
import net.catchpole.B9.devices.gps.command.ChangeBuad;
import net.catchpole.B9.devices.gps.command.GpsCommandSender;
import net.catchpole.B9.devices.gps.command.LineWriter;
import net.catchpole.B9.devices.gps.command.MessageIntervals;
import net.catchpole.B9.devices.gps.listener.LocationListener;
import net.catchpole.B9.devices.gps.listener.MessageListener;
import net.catchpole.B9.devices.gps.listener.VectorListener;
import net.catchpole.B9.devices.serial.PiCommPort;
import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;
import net.catchpole.B9.spacial.Vector;

import java.io.IOException;

public class SerialGps implements Gps, Compass, Speedometer {
    private static final int FACTORY_BAUD = 9600;
    private static final int ACTIVE_BAUD = 38400;
    private final GpsParser gpsParser = new GpsParser();
    private final MessageIntervals messageIntervals = new MessageIntervals();
    private final PiCommPort piCommPort;
    private volatile Vector vector;
    private volatile Location location;

    public SerialGps() throws IOException {
        changeBaud();
        addListeners();
        this.piCommPort = new PiCommPort(ACTIVE_BAUD, new LineWriter() {
            public void writeLine(String value) {
                SerialGps.this.gpsParser.parse(value);
            }
        });
        // tell the GPS to only send the messages we are interested in
        GpsCommandSender gpsCommandSender = new GpsCommandSender(piCommPort);
        gpsCommandSender.send(messageIntervals);
    }

    private void changeBaud() {
        // switch GPS to higher baud rate. if the device is currently on the higher baud, this should have no effect
        PiCommPort piCommPort = new PiCommPort(FACTORY_BAUD);
        piCommPort.writeLine("\r\n");
        GpsCommandSender gpsCommandSender = new GpsCommandSender(piCommPort);
        gpsCommandSender.send(new ChangeBuad(ACTIVE_BAUD));
        gpsCommandSender.delay();
        piCommPort.close();
    }

    public void addListener(MessageListener messageListener) {
        this.gpsParser.addListener(messageListener);
    }

    private void addListeners() {
        this.gpsParser.addListener(new VectorListener() {
            public void listen(Vector vector) {
                SerialGps.this.vector = vector;
            }
        });
        this.messageIntervals.setGPVTGInterval(1);

        this.gpsParser.addListener(new LocationListener() {
            public void listen(Location location) {
                SerialGps.this.location = location;
            }
        });
        this.messageIntervals.setGPGGAInterval(1);
    }

    public Location getLocation() {
        return this.location;
    }

    public Heading getHeading() {
        Vector vector = this.vector;
        return vector == null ? null : vector.getHeading();
    }

    public double getVelocity() {
        Vector vector = this.vector;
        return vector == null ? 0.0d : vector.getVelocity();
    }

    public static void main(String[] args) throws Exception {
        new SerialGps();
    }
}
