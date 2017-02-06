package net.catchpole.B9.tools;

import net.catchpole.B9.devices.rockblock.RockBlock;
import net.catchpole.B9.devices.serial.DataListener;
import net.catchpole.B9.devices.serial.PiSerialPort;

public class RockBlockTest {
    public static void main(String[] args) throws Exception {
        new RockBlockTest();
    }

    public RockBlockTest() throws Exception {
        RockBlock rockBlock = new RockBlock(new PiSerialPort());
        rockBlock.setDataListener(new DataListener() {
            @Override
            public void receive(byte[] data, int len) {
                System.out.println("Received: " + new String(data, 0, len));
            }
        });
        try {
            rockBlock.connect();
            rockBlock.clearSendingBuffer();
//            rockBlock.sendBinaryMessage(("SLPSLPSLPS " + new Date()).getBytes());
            rockBlock.sendAndReceive();
            System.out.println(rockBlock.getStatus());
        } finally {
            rockBlock.close();
        }
        System.exit(0);
    }
}
