package net.catchpole.B9.devices.gps.listener;

public interface MessageListener<T> {
    void listen(T type);
}
