package net.catchpole.B9.codec.field;

public interface Flags {
    boolean readFlag();

    void writeFlag(boolean value);
}
