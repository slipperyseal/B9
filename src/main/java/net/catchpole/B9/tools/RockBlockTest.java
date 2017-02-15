package net.catchpole.B9.tools;

import net.catchpole.B9.devices.rockblock.RockBlock;
import net.catchpole.B9.devices.serial.DataListener;
import net.catchpole.B9.devices.serial.PiSerialPort;

import java.util.Date;

public class RockBlockTest {
    public static void main(String[] args) throws Exception {
        new RockBlockTest(args[0]);
    }

    public RockBlockTest(String port) throws Exception {
        RockBlock rockBlock = new RockBlock(new PiSerialPort(port));
        rockBlock.setDataListener(new DataListener() {
            @Override
            public void receive(byte[] data, int len) {
                System.out.println("Received: " + new String(data, 0, len));
            }
        });
        try {
            rockBlock.initialize();
            rockBlock.clearSendingBuffer();

            rockBlock.sendBinaryMessage(("SLPSLPSLPS " + new Date()).getBytes());
            System.out.println(new String(rockBlock.readBinaryMessage()));

            System.out.println(rockBlock.getManufacturer());
            System.out.println(rockBlock.getModel());
            System.out.println(rockBlock.getRevision());

            rockBlock.waitForReception();
//            rockBlock.sendAndReceive();
            System.out.println(rockBlock.getStatus());
        } finally {
            rockBlock.close();
        }
        System.exit(0);
    }
}
