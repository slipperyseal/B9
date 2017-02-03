package net.catchpole.B9.tools;

import net.catchpole.B9.devices.rockblock.RockBlock;
import net.catchpole.B9.devices.serial.PiSerialPort;

public class RockBlockTest {
    public static void main(String[] args) throws Exception {
        new RockBlockTest();
    }

    public RockBlockTest() throws Exception {
        RockBlock rockBlock = new RockBlock(new PiSerialPort());
        rockBlock.connect();
        System.out.println(rockBlock.getStatus());
        rockBlock.waitForReception();
        System.out.println(rockBlock.getStatus());
    }
}
