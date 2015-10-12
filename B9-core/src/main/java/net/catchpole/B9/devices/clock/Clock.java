package net.catchpole.B9.devices.clock;

public interface Clock {
    long getCurrentTime();

    void sleep(long millis);
}
