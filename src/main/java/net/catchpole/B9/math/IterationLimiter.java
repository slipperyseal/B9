package net.catchpole.B9.math;

public class IterationLimiter {
    private final int total;
    private int value;

    public IterationLimiter(int total) {
        this.total = total;
    }

    public boolean next() {
        if (value <= 0) {
            value = total;
            return true;
        }
        value--;
        return false;
    }

    public void reset() {
        value = 0;
    }
}
