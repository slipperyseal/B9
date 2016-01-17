package net.catchpole.B9.devices.serial;

import net.catchpole.B9.devices.gps.SerialGps;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SerialSocketTest {
    @Test
    public void test() throws Exception {
        new Thread() {
            @Override
            public void run() {
                try {
                    new SocketListenerSerialPort(new TermiosSerialPort(null), 5000, 9600).accept();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Thread.sleep(2000);

        SerialGps serialGps = new SerialGps(new SocketSerialPort("localhost", 5000));
//
        for(;;) {
            Thread.sleep(1000);
        }
    }
}
