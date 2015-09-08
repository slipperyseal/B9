package net.catchpole.B9.devices.hardware;

public interface Clock {
    public long getCurrentTime();

    public void sleep(long millis);
}
