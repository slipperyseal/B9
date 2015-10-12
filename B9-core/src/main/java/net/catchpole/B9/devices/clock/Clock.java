package net.catchpole.B9.devices.clock;

public interface Clock {
    public long getCurrentTime();

    public void sleep(long millis);
}
