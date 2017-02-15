package net.catchpole.B9.devices.gps;

import net.catchpole.B9.devices.Device;
import net.catchpole.B9.devices.compass.Compass;
import net.catchpole.B9.devices.gps.command.*;
import net.catchpole.B9.devices.gps.listener.LocationListener;
import net.catchpole.B9.devices.gps.listener.MessageListener;
import net.catchpole.B9.devices.gps.listener.VectorListener;
import net.catchpole.B9.devices.serial.SerialConnection;
import net.catchpole.B9.devices.serial.SerialPort;
import net.catchpole.B9.devices.serial.binding.LineWriterDataListener;
import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;
import net.catchpole.B9.spacial.Vector;

import java.io.IOException;

public class SerialGps implements Gps, Compass, Speedometer, Device {
    private static final int FACTORY_BAUD = 9600;
    private static final int ACTIVE_BAUD = 38400;
    private final GpsParser gpsParser = new GpsParser();
    private final MessageIntervals messageIntervals = new MessageIntervals();
    private SerialConnection serialConnection;
    private volatile Vector vector;
    private volatile Location location;
    private final SerialPort serialPort;

    public SerialGps(SerialPort serialPort) throws IOException, InterruptedException {
        this.serialPort = serialPort;
        addListeners();
    }

    @Override
    public void initialize() throws Exception {
        changeBaud(serialPort);
        this.serialConnection = serialPort.openConnection(ACTIVE_BAUD);
        this.serialConnection.setDataListener(new LineWriterDataListener(new LineWriter() {
            @Override
            public void writeLine(String value) {
                SerialGps.this.gpsParser.parse(value);
            }
        }));
        // tell the GPS to only send the messages we are interested in
        GpsCommandSender gpsCommandSender = new GpsCommandSender(new ByteLineWriter(this.serialConnection));
        gpsCommandSender.send(messageIntervals);
        gpsCommandSender.send(new FixUpdateRate(500));
        gpsCommandSender.send(new NmeaUpdateRate(500));
    }

    @Override
    public boolean isHealthy() throws Exception {
        return true;
    }

    @Override
    public void close() throws Exception {

    }

    private void changeBaud(SerialPort serialPort) throws IOException, InterruptedException {
        SerialConnection serialConnection = serialPort.openConnection(FACTORY_BAUD);
        // switch GPS to higher baud rate. if the device is currently on the higher baud, this should have no effect
        serialConnection.write("\r\n".getBytes());
        GpsCommandSender gpsCommandSender = new GpsCommandSender(new ByteLineWriter(serialConnection));
        gpsCommandSender.send(new ChangeBuad(ACTIVE_BAUD));
        gpsCommandSender.delay();
        serialConnection.close();
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

    class ByteLineWriter implements LineWriter {
        SerialConnection serialConnection;

        public ByteLineWriter(SerialConnection serialConnection) {
            ByteLineWriter.this.serialConnection = serialConnection;
        }
        @Override
        public void writeLine(String value) {
            try {
                ByteLineWriter.this.serialConnection.write(value.getBytes());
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }
}
