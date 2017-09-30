package net.catchpole.B9.tools;

import net.catchpole.B9.devices.rockblock.RockBlock;
import net.catchpole.B9.devices.serial.DataListener;
import net.catchpole.B9.devices.serial.PiSerialPort;
import net.catchpole.B9.lang.Arguments;

public class RockBlockTest {
    public static void main(String[] args) throws Exception {
        new RockBlockTest(args);
    }

    public RockBlockTest(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);
        String device = arguments.getArgument("-device");
        String message = arguments.getArgument("-message", null);
        int minimumReception = arguments.getIntArgument("-minimumreception", 2);
        int waitForReception = arguments.getIntArgument("-waitforreception", 60 * 3);

        RockBlock rockBlock = new RockBlock(new PiSerialPort(device));
        rockBlock.setDataListener(new DataListener() {
            @Override
            public void receive(byte[] data, int len) {
                System.out.println("Received: " + new String(data, 0, len));
            }
        });
        try {
            rockBlock.initialize();
            rockBlock.clearSendingBuffer();

            if (message != null) {
                rockBlock.sendBinaryMessage(message.getBytes());
            }
            System.out.println(new String(rockBlock.readBinaryMessage()));

            System.out.println(rockBlock.getManufacturer());
            System.out.println(rockBlock.getModel());
            System.out.println(rockBlock.getRevision());

            rockBlock.waitForReception(waitForReception*1000, minimumReception);
            if (message != null) {
                rockBlock.sendAndReceive();
            }
            System.out.println(rockBlock.getStatus());
        } finally {
            rockBlock.close();
        }
        System.exit(0);
    }
}
