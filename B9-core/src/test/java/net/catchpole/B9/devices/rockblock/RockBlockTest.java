package net.catchpole.B9.devices.rockblock;

import net.catchpole.B9.devices.serial.SocketSerialPort;
import org.junit.Test;

public class RockBlockTest {
    @Test
    public void test() throws Exception {
        RockBlock rockBlock = new RockBlock(new SocketSerialPort("localhost", 4000));
        rockBlock.connect();

        Thread.sleep(5000);
    }
}
