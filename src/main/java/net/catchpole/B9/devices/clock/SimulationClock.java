package net.catchpole.B9.devices.clock;

public class SimulationClock implements Clock {
    private long time;
    private double fraction;

    public SimulationClock() {
    }

    /**
     * Fraction of realtime to sleep.
     * 0.0 for no sleeping.
     * 0.1 would be 10% of realtime.
     * 1.0 would be realtime.
     * 2.0 would be 200% of realtime.
     */
    public SimulationClock(double fraction) {
        this.fraction = fraction;
    }

    public long getCurrentTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void sleep(long value) {
        this.time += value;

        if (fraction != 0.0) {
            try {
                Thread.sleep((long)(value*fraction));
            } catch (Exception e) {
            }
        }
    }
}