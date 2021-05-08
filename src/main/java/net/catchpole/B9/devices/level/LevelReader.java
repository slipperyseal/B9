package net.catchpole.B9.devices.level;

import java.io.IOException;

public interface LevelReader {
    int readLevel(int channel) throws IOException;

    void blink(int led) throws IOException;
}
