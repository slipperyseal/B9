package net.catchpole.B9.devices.level;

import java.io.IOException;
import java.util.Random;

public class SimulationLevelReader implements LevelReader {
    private int level;
    private Random random = new Random();

    public SimulationLevelReader(int level) {
        this.level = level;
    }

    @Override
    public int readLevel(int channel) throws IOException {
        return level + random.nextInt(10);
    }

    @Override
    public void blink(int led) throws IOException {
    }
}
