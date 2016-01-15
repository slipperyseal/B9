package net.catchpole.B9.devices.serial;

import net.catchpole.B9.devices.gps.SerialGps;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SerialGpsTest {
    @Test
    public void liveTest() throws Exception {
        SerialGps serialGps2 = new SerialGps(new TermiosSerialPort(null));

        for(;;) {
            Thread.sleep(1000);
        }
    }
}
