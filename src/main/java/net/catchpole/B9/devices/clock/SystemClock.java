package net.catchpole.B9.devices.clock;

public class SystemClock implements Clock {
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }
}
