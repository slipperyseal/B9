package net.catchpole.B9.devices.controlpad;

public interface ClassicControllerListener {
    public void buttonDown(int button);

    public void buttonUp(int button);

    public void leftJoystick(int horizontal, int vertical);

    public void rightJoystick(int horizontal, int vertical);
}
