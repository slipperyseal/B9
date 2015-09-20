package net.catchpole.B9.tools;

import net.catchpole.B9.devices.serial.PiCommPort;

public class CommLogger extends PiCommPort {
    @Override
    public void process(String line) {
        System.out.println(line);
    }

    public static void main(String[] args) throws Exception {
        CommLogger commLogger = new CommLogger();

        for (;;) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
