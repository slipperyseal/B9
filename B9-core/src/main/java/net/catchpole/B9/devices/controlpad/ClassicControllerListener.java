package net.catchpole.B9.devices.controlpad;

public interface ClassicControllerListener {
    void buttonDown(int button);

    void buttonUp(int button);

    void leftJoystick(int horizontal, int vertical);

    void rightJoystick(int horizontal, int vertical);
}
