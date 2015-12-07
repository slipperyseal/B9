package net.catchpole.B9.tools;

import net.catchpole.B9.devices.gps.command.LineWriter;
import net.catchpole.B9.devices.serial.PiCommPort;

public class CommLogger extends PiCommPort {
    public CommLogger() {
        super(9600, new LineWriter() {
            @Override
            public void writeLine(String value) {
                System.out.println(value);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        CommLogger commLogger = new CommLogger();

        for (;;) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
