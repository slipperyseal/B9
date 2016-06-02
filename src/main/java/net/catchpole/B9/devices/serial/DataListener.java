package net.catchpole.B9.devices.serial;

public interface DataListener {
    void receive(byte[] data, int len);
}
