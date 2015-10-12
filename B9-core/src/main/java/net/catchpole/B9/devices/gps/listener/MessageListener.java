package net.catchpole.B9.devices.gps.listener;

public interface MessageListener<T> {
    public void listen(T type);
}
