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
    private final PiCommPort piCommPort;
    private final GpsParser gpsParser = new GpsParser();
    private volatile Vector vector;
    private volatile Location location;

    public SerialGps() throws IOException {
        setBaud();
        addListeners();
        this.piCommPort = new PiCommPort(38400, new LineWriter() {
            public void writeLine(String value) {
                System.out.println(value);
                SerialGps.this.gpsParser.parse(value);
            }
        });
        GpsCommandSender gpsCommandSender = new GpsCommandSender(piCommPort);
        MessageIntervals messageIntervals = new MessageIntervals();
        messageIntervals.setGPGGAInterval(1);
        messageIntervals.setGPVTGInterval(1);
        gpsCommandSender.send(messageIntervals);
    }

    private void setBaud() {
        PiCommPort piCommPort = new PiCommPort(9600);
        piCommPort.writeLine("\r\n");
        GpsCommandSender gpsCommandSender = new GpsCommandSender(piCommPort);
        gpsCommandSender.send(new ChangeBuad(38400));
        gpsCommandSender.delay();
        piCommPort.close();
    }

    private void addListeners() {
        this.gpsParser.addListener(new VectorListener() {
            public void listen(Vector vector) {
                SerialGps.this.vector = vector;
            }
        });

        this.gpsParser.addListener(new LocationListener() {
            public void listen(Location location) {
                SerialGps.this.location = location;
            }
        });
    }

    public void addListener(MessageListener messageListener) {
        this.gpsParser.addListener(messageListener);
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
