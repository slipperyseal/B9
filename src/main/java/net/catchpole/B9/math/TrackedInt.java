package net.catchpole.B9.math;

public class TrackedInt {
    private int value;
    private int change;

    public void update(int value) {
        this.change = this.value^value;
        this.value = value;
    }

    public int getChangedBits() {
        return this.change;
    }

    public boolean hasChanged() {
        return this.change != 0;
    }

    public boolean isBitSet(int bit) {
        return (this.value & (1<<bit)) != 0;
    }

    public boolean hasBitChanged(int bit) {
        return (this.change & (1<<bit)) != 0;
    }

    public int getValue() {
        return this.value;
    }
}
