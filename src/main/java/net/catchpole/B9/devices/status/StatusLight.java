package net.catchpole.B9.devices.status;

import com.pi4j.io.gpio.*;
import net.catchpole.B9.devices.Device;

public class StatusLight implements Device {
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput led;

    public StatusLight() {
        this.led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "Status Light", PinState.HIGH);
    }

    public StatusLight(int pin) {
        this.led = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pin), "Status Light", PinState.HIGH);
    }

    @Override
    public void initialize() throws Exception {
        led.blink(200, 2000);
    }

    @Override
    public boolean isHealthy() throws Exception {
        return true;
    }

    @Override
    public void close() throws Exception {
    }
}
