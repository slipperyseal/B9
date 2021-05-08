package net.catchpole.B9.devices.level;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import net.catchpole.B9.devices.Device;

import java.io.IOException;

public class I2CLevelReader implements LevelReader, Device {
    private I2CBus bus;
    private I2CDevice device;

    private static final int COMMAND_READ = 0x10;
    private static final int COMMAND_BLINK = 0x20;

    @Override
    public void initialize() throws Exception {
        if (this.bus == null) {
            this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        }
        if (this.device == null) {
            this.device = bus.getDevice(0x10);
        }
    }

    @Override
    public boolean isHealthy() throws Exception {
        return true;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public int readLevel(int channel) throws IOException  {
        this.device.write((byte)(COMMAND_READ | (channel & 0x0f)));
        return device.read() | (device.read() << 8);
    }

    @Override
    public void blink(int led) throws IOException {
        this.device.write((byte)(COMMAND_BLINK | (led & 0x0f)));
    }
}
