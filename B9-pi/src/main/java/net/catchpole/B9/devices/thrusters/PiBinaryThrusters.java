package net.catchpole.B9.devices.thrusters;

import com.pi4j.io.gpio.*;
import net.catchpole.B9.devices.esc.BlueESCData;

// binary thrusters are relay driven motors which can only go forward or reverse at one speed (binary - on or off)
public class PiBinaryThrusters implements Thrusters {
    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalOutput leftReverse;
    private final GpioPinDigitalOutput leftForward;
    private final GpioPinDigitalOutput rightReverse;
    private final GpioPinDigitalOutput rightForward;

    public PiBinaryThrusters() {
        this.leftReverse = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "leftReverse", PinState.HIGH);
        this.leftForward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "leftForward", PinState.HIGH);
        this.rightReverse = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "rightReverse", PinState.HIGH);
        this.rightForward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "rightForward", PinState.HIGH);

        // turn off thrusters when shutting down
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                update(0.0, 0.0);
            }
        });

        this.update(0.0, 0.0);
    }

    public void shutdown() {
        this.gpio.shutdown();
    }

    public void update(double left, double right) {
        if (left != 0.0) {
            if (left > 0.0) {
                leftForward.low();
                leftReverse.high();
            } else {
                leftForward.high();
                leftReverse.low();
            }
        } else {
            leftForward.high();
            leftReverse.high();
        }
        if (right != 0.0) {
            if (right > 0.0) {
                rightForward.low();
                rightReverse.high();
            } else {
                rightForward.high();
                rightReverse.low();
            }
        } else {
            rightForward.high();
            rightReverse.high();
        }
    }

    public BlueESCData getLeftData() {
        return null;
    }

    public BlueESCData getRightData() {
        return null;
    }
}