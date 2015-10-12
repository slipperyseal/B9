package net.catchpole.B9.devices.esc;

public interface ESC {
    void initialize();

    boolean update(int throttle);
}
