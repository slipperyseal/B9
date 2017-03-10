package net.catchpole.B9.devices.clock;

public class TimeTracking {
    private Clock clock;
    private long lastUpdatedTime;

    public TimeTracking(Clock clock) {
        this.clock = clock;
        this.lastUpdatedTime = clock.getCurrentTime();
    }

    public long getTimePassed() {
        long time = clock.getCurrentTime();
        try {
            return time - lastUpdatedTime;
        } finally {
            lastUpdatedTime = time;
        }
    }
}
