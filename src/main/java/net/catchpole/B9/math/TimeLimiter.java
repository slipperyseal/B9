package net.catchpole.B9.math;

import net.catchpole.B9.devices.clock.Clock;

public class TimeLimiter {
    private final Clock clock;
    private final int millis;
    private long waitUntil;

    public TimeLimiter(Clock clock, int millis) {
        this.clock = clock;
        this.millis = millis;
    }

    public boolean next() {
        if (waitUntil == 0 || clock.getCurrentTime() >= waitUntil) {
            this.waitUntil = clock.getCurrentTime() + this.millis;
            return true;
        }
        return false;
    }

    public void reset() {
        waitUntil = 0;
    }
}
