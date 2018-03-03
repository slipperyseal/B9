package net.catchpole.B9.devices.esc;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import net.catchpole.B9.lang.Throw;

import java.io.IOException;

public class PCA9685BasicESC {
    private final static int PCA9685_ADDRESS = 0x40;
    private final static int MODE1 = 0x00;
    private final static int PRESCALE = 0xFE;
    private final static int ON_L = 0x06;
    private final static int ON_H = 0x07;
    private final static int OFF_L = 0x08;
    private final static int OFF_H = 0x09;

    private I2CBus i2CBus;
    private I2CDevice i2CDevice;
    private BasicESC[] thrusters = new BasicESC[16];

    private double frequency;
    private double fullReverse = 1.0d;
    private double throttleZero = 1.5d;
    private double centerPad = 0.05d;
    private double fullForward = 2.0d;

    public PCA9685BasicESC() {
        this(PCA9685_ADDRESS);
    }

    public PCA9685BasicESC(int address) {
        try {
            this.i2CBus = I2CFactory.getInstance(I2CBus.BUS_1);
            this.i2CDevice = i2CBus.getDevice(address);
            this.i2CDevice.write(MODE1, (byte) 0x00);
        } catch (Exception e) {
            Throw.unchecked(e);
        }
        this.setPWMFrequency(60.0d);

        allZero();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                allZero();
            }
        });
    }

    public void setTiming(double fullReverse, double throttleZero, double fullForward, double centerPad) {
        this.fullReverse = fullReverse;
        this.throttleZero = throttleZero;
        this.fullForward = fullForward;
        this.centerPad = centerPad;
    }

    public synchronized ESC createThruster(int channel) {
        if (thrusters[channel] == null) {
            thrusters[channel] = new BasicESC(channel);
        }
        return thrusters[channel];
    }

    private void allZero() {
        for (int x=0;x<16;x++) {
            this.setPWM(x, 0, 0);
        }
    }

    public class BasicESC implements ESC {
        private int channel;

        BasicESC(int channel) {
            this.channel = channel;
            setPulseRate(channel, throttleZero);
        }

        public void update(double thrust) {
            if (thrust == 0.0d) {
                setPulseRate(channel, throttleZero);
            } else {
                setPulseRate(channel, thrust >= 0.0d ?
                        throttleZero + centerPad + ((fullForward - throttleZero - centerPad) *
                                (thrust > 1.0d ? 1.0d : thrust)) :
                        throttleZero - centerPad - ((throttleZero - fullReverse - centerPad) *
                                (0.0d-(thrust < -1.0d ? -1.0d : thrust))));
            }
        }

        public void close() throws Exception {
            setPWM(channel, 0, 0);
        }
    }

    void close() throws Exception {
        for (BasicESC basicESC : thrusters) {
            if (basicESC != null) {
                basicESC.close();
            }
        }
    }

    private void setPWMFrequency(double freq) {
        this.frequency = freq;
        double preScaleVal = 25000000.0;
        preScaleVal /= 4096.0;
        preScaleVal /= (freq * 0.9d); // correct for freq overshoot
        preScaleVal -= 1.0;
        preScaleVal = Math.floor(preScaleVal + 0.5);
        try {
            byte lastMode = (byte) i2CDevice.read(MODE1);
            byte updatedMode = (byte) ((lastMode & 0x7F) | 0x10);
            i2CDevice.write(MODE1, updatedMode);
            i2CDevice.write(PRESCALE, (byte) preScaleVal);
            i2CDevice.write(MODE1, lastMode);
            Thread.sleep(5);
            i2CDevice.write(MODE1, (byte) (lastMode | 0x80));
        } catch (Exception ioe) {
            Throw.unchecked(ioe);
        }
    }

    private void setPWM(int channel, int on, int off) throws IllegalArgumentException {
        try {
            byte[] data = new byte[] { (byte) on, (byte) (on >> 8), (byte) off, (byte) (off >> 8) };
            i2CDevice.write( (4 * channel) + ON_L, data, 0, data.length);
        } catch (IOException ioe) {
            Throw.unchecked(ioe);
        }
    }

    public void setPulseRate(int channel, double pulseMS) {
        if (pulseMS == 0.0d) {
            this.setPWM(channel, 0, 0);
        } else {
            double pulseLength = 1000000;
            pulseLength /= this.frequency;
            pulseLength /= 4096;
            int pulse = (int) (pulseMS * 1000);
            pulse /= pulseLength;
            this.setPWM(channel, 0, pulse);
        }
    }

    @Override
    public String toString() {
        return "PCA9685BasicESC{" +
                "frequency=" + frequency +
                ", fullReverse=" + fullReverse +
                ", throttleZero=" + throttleZero +
                ", centerPad=" + centerPad +
                ", fullForward=" + fullForward +
                '}';
    }
}