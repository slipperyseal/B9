package net.catchpole.B9.tools;

import net.catchpole.B9.devices.status.StatusLight;
import net.catchpole.B9.lang.Arguments;

public class StatusLightTest {
    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);

        int pin = arguments.getArgumentProperty("-pin", -1);
        StatusLight statusLight = pin == -1 ? new StatusLight() : new StatusLight(pin);
        statusLight.initialize();

        for (;;) {
            Thread.sleep(1000000);
        }
    }
}
