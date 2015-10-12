package net.catchpole.B9.devices.controlpad;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import net.catchpole.B9.math.TrackedInt;

// Nintendo Wii Classic Controller - tested with a cheap un-official controller
public class ClassicController {
    public static final int BUTTON_ZR = 1<<1;
    public static final int BUTTON_START = 1<<2;
    public static final int BUTTON_HOME = 1<<3;
    public static final int BUTTON_SELECT = 1<<4;
    public static final int BUTTON_ZL = 1<<5;
    public static final int BUTTON_PAD_DOWN = 1<<6;
    public static final int BUTTON_PAD_RIGHT = 1<<7;
    public static final int BUTTON_PAD_UP = 1<<8;
    public static final int BUTTON_PAD_LEFT = 1<<9;
    public static final int BUTTON_RIGHT = 1<<10;
    public static final int BUTTON_X = 1<<11;
    public static final int BUTTON_A = 1<<12;
    public static final int BUTTON_Y = 1<<13;
    public static final int BUTTON_B = 1<<14;
    public static final int BUTTON_LEFT = 1<<15;

    private final I2CBus bus;
    private final I2CDevice device;
    private final TrackedInt buttons = new TrackedInt();
    private final TrackedInt leftHorizontal = new TrackedInt();
    private final TrackedInt leftVertical = new TrackedInt();
    private final TrackedInt rightHorizontal = new TrackedInt();
    private final TrackedInt rightVertical = new TrackedInt();

    private ClassicControllerListener classicControllerListener;

    public ClassicController() throws Exception {
        this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        this.device = bus.getDevice(0x52);
    }

    public void oblivion() {
        byte[] bytes = new byte[6];
        for (;;) {
            try {
                int len = device.read(0x00, bytes, 0, bytes.length);
                if (len == 6) {
                    processButtons(bytes);
                    processJoysticks(bytes);
                }
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }

    public void setClassicControllerListener(ClassicControllerListener classicControllerListener) {
        this.classicControllerListener = classicControllerListener;
    }

    private void processButtons(byte[] bytes) {
        this.buttons.update(getButtonsValue(bytes));

        if (buttons.hasChanged() && classicControllerListener != null) {
            for (int x=0;x<16;x++) {
                if (buttons.hasBitChanged(x)) {
                    if (buttons.isBitSet(x)) {
                        classicControllerListener.buttonDown(x);
                    } else {
                        classicControllerListener.buttonUp(x);
                    }
                }
            }
        }
    }

    private void processJoysticks(byte[] bytes) {
        leftHorizontal.update((bytes[0] & 0x3f)-32);
        leftVertical.update((bytes[1] & 0x3f)-32);

        rightHorizontal.update((((bytes[0] & 0xc0) >> 3) | ((bytes[1] & 0xc0) >> 5) | ((bytes[2] & 0x80) >> 7)) - 16);
        rightVertical.update((bytes[2] & 0x1f)-16);

        if (classicControllerListener != null) {
            if (leftHorizontal.hasChanged() || leftVertical.hasChanged()) {
                classicControllerListener.leftJoystick(leftHorizontal.getValue(), leftVertical.getValue());
            }
            if (rightHorizontal.hasChanged() || rightVertical.hasChanged()) {
                classicControllerListener.rightJoystick(rightHorizontal.getValue(), rightVertical.getValue());
            }
        }
    }

    private int getButtonsValue(byte[] bytes) {
        return (~bytes[4] & 0xff) | ((~bytes[5] & 0xff) <<8);
    }
}