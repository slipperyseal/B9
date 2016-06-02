package net.catchpole.B9.devices.gps;

import junit.framework.TestCase;
import net.catchpole.B9.devices.gps.command.*;
import org.junit.Test;

public class GpsCommandSenderTest {
    @Test
    public void testChangeBaud() {
        GpsCommandSender gpsCommandSender = new GpsCommandSender(new LineWriter() {
            @Override
            public void writeLine(String value) {
                TestCase.assertEquals("$PMTK251,38400*27\r\n", value);
            }
        });
        gpsCommandSender.send(new ChangeBuad(38400));
    }

    @Test
    public void testNmeaUpdateRate() {
        GpsCommandSender gpsCommandSender = new GpsCommandSender(new LineWriter() {
            @Override
            public void writeLine(String value) {
                TestCase.assertEquals("$PMTK220,200*2C\r\n", value);
            }
        });
        gpsCommandSender.send(new NmeaUpdateRate(200));
    }

    @Test
    public void testFixUpdateRate() {
        GpsCommandSender gpsCommandSender = new GpsCommandSender(new LineWriter() {
            @Override
            public void writeLine(String value) {
                TestCase.assertEquals("$PMTK300,500,0,0,0,0*28\r\n", value);
            }
        });
        gpsCommandSender.send(new FixUpdateRate(500));
    }

    @Test
    public void testMessageIntervals() {
        GpsCommandSender gpsCommandSender = new GpsCommandSender(new LineWriter() {
            @Override
            public void writeLine(String value) {
                TestCase.assertEquals("$PMTK314,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0*28\r\n", value);
            }
        });
        MessageIntervals messageIntervals = new MessageIntervals();
        messageIntervals.setGPVTGInterval(1);
        messageIntervals.setGPGGAInterval(1);
        gpsCommandSender.send(messageIntervals);
    }


}
