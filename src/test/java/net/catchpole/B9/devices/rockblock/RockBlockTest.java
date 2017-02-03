package net.catchpole.B9.devices.rockblock;

import net.catchpole.B9.devices.serial.SocketSerialPort;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RockBlockTest {
    @Test
    public void test() throws Exception {
        RockBlock rockBlock = new RockBlock(new SocketSerialPort("localhost", 4000));
        rockBlock.connect();
        System.out.println(rockBlock.getStatus());

        rockBlock.sendTestMessage("oh HAI MelbJVM!");
        if (rockBlock.waitForReception()) {
//            rockBlock.sendTestMessage("oh HAI MelbJVM!");
//            rockBlock.initiateSession();
        }
        System.out.println(rockBlock.getStatus());
        Thread.sleep(5000);
    }
}
