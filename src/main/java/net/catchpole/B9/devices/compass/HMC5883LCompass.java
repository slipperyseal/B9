package net.catchpole.B9.devices.compass;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import net.catchpole.B9.devices.Device;
import net.catchpole.B9.spacial.Heading;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HMC5883LCompass implements Compass, Device {
    private static final int HMC5883L_ADDRESS            = 0x1E;

    private static final int HMC5883L_RA_CONFIG_A        = 0x00;
    private static final int HMC5883L_RA_CONFIG_B        = 0x01;
    private static final int HMC5883L_RA_MODE            = 0x02;
    private static final int HMC5883L_RA_DATAX_H         = 0x03;
    private static final int HMC5883L_RA_DATAX_L         = 0x04;
    private static final int HMC5883L_RA_DATAZ_H         = 0x05;
    private static final int HMC5883L_RA_DATAZ_L         = 0x06;
    private static final int HMC5883L_RA_DATAY_H         = 0x07;
    private static final int HMC5883L_RA_DATAY_L         = 0x08;
    private static final int HMC5883L_RA_STATUS          = 0x09;
    private static final int HMC5883L_RA_ID_A            = 0x0A;
    private static final int HMC5883L_RA_ID_B            = 0x0B;
    private static final int HMC5883L_RA_ID_C            = 0x0C;

    private static final int HMC5883L_CRA_AVERAGE_BIT    = 6;
    private static final int HMC5883L_CRA_AVERAGE_LENGTH = 2;
    private static final int HMC5883L_CRA_RATE_BIT       = 4;
    private static final int HMC5883L_CRA_RATE_LENGTH    = 3;
    private static final int HMC5883L_CRA_BIAS_BIT       = 1;
    private static final int HMC5883L_CRA_BIAS_LENGTH    = 2;

    private static final int HMC5883L_AVERAGING_1        = 0x00;
    private static final int HMC5883L_AVERAGING_2        = 0x01;
    private static final int HMC5883L_AVERAGING_4        = 0x02;
    private static final int HMC5883L_AVERAGING_8        = 0x03;

    private static final int HMC5883L_RATE_0P75          = 0x00;
    private static final int HMC5883L_RATE_1P5           = 0x01;
    private static final int HMC5883L_RATE_3             = 0x02;
    private static final int HMC5883L_RATE_7P5           = 0x03;
    private static final int HMC5883L_RATE_15            = 0x04;
    private static final int HMC5883L_RATE_30            = 0x05;
    private static final int HMC5883L_RATE_75            = 0x06;

    private static final int HMC5883L_BIAS_NORMAL        = 0x00;
    private static final int HMC5883L_BIAS_POSITIVE      = 0x01;
    private static final int HMC5883L_BIAS_NEGATIVE      = 0x02;

    private static final int HMC5883L_CRB_GAIN_BIT       = 7;
    private static final int HMC5883L_CRB_GAIN_LENGTH    = 3;

    private static final int HMC5883L_GAIN_1370          = 0x00;
    private static final int HMC5883L_GAIN_1090          = 0x01;
    private static final int HMC5883L_GAIN_820           = 0x02;
    private static final int HMC5883L_GAIN_660           = 0x03;
    private static final int HMC5883L_GAIN_440           = 0x04;
    private static final int HMC5883L_GAIN_390           = 0x05;
    private static final int HMC5883L_GAIN_330           = 0x06;
    private static final int HMC5883L_GAIN_220           = 0x07;

    private static final int HMC5883L_MODEREG_BIT        = 1;
    private static final int HMC5883L_MODEREG_LENGTH     = 2;

    private static final int HMC5883L_MODE_CONTINUOUS    = 0x00;
    private static final int HMC5883L_MODE_SINGLE        = 0x01;
    private static final int HMC5883L_MODE_IDLE          = 0x02;

    private static final int HMC5883L_STATUS_LOCK_BIT    = 1;
    private static final int HMC5883L_STATUS_READY_BIT   = 0;

    private Map<Double,Gauss> gaussMap = new HashMap<Double, Gauss>();
    private I2CBus bus;
    private I2CDevice device;
    private double scale;

    public HMC5883LCompass() throws Exception {
        this.addGauss(0.88, 0x00, 0.73d);
        this.addGauss(1.3d, 0x01, 0.92d);
        this.addGauss(1.9d, 0x02, 1.22d);
        this.addGauss(2.5d, 0x04, 2.27d);
        this.addGauss(4.7d, 0x05, 2.56d);
        this.addGauss(5.6d, 0x06, 3.03d);
        this.addGauss(8.1d, 0x07, 4.35);
    }

    public void initialize() throws Exception {
        if (this.bus == null) {
            this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        }
        if (this.device == null) {
            this.device = bus.getDevice(HMC5883L_ADDRESS);
        }
        this.device.write(HMC5883L_RA_CONFIG_A,
                (byte) ((HMC5883L_AVERAGING_8 << (HMC5883L_CRA_AVERAGE_BIT - HMC5883L_CRA_AVERAGE_LENGTH + 1)) |
                        (HMC5883L_RATE_15 << (HMC5883L_CRA_RATE_BIT - HMC5883L_CRA_RATE_LENGTH + 1)) |
                        (HMC5883L_BIAS_NORMAL << (HMC5883L_CRA_BIAS_BIT - HMC5883L_CRA_BIAS_LENGTH + 1))));
        setScale(1.3d);
        this.device.write(HMC5883L_RA_MODE,
                (byte) (HMC5883L_MODE_CONTINUOUS << (HMC5883L_MODEREG_BIT - HMC5883L_MODEREG_LENGTH + 1)));
    }

    public boolean isHealthy() throws Exception {
        byte[] bytes = new byte[3];
        synchronized (this.bus) {
            return device.read(HMC5883L_RA_ID_A, bytes, 0, 3) == 3 && bytes[0] == 'H' && bytes[1] == '4' && bytes[2] == '3';
        }
    }

    public void close() throws Exception {
        this.bus = null;
        this.device = null;
    }

    public Heading getHeading() {
        try {
            byte[] bytes = new byte[6];
            synchronized (this.bus) {
                if (device.read(HMC5883L_RA_DATAX_H, bytes, 0, 6) != 6) {
                    throw new IOException(HMC5883LCompass.class.getName() + " read fail");
                }
            }
            short rx = (short)(((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff));
            short rz = (short)(((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff));
            short ry = (short)(((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff));

            double x = rx * scale;
            double y = ry * scale;
            double z = rz * scale;

            double heading = Math.atan2(y,x) * (180.0d / Math.PI) + 180.0d;

            return new Heading(heading);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void addGauss(double gauss, int register, double scale) {
        Gauss g = new Gauss();
        g.gauss = gauss;
        g.register = register;
        g.scale = scale;
        gaussMap.put(g.gauss, g);
    }

    public void setScale(double gauss) throws IOException {
        Gauss g = gaussMap.get(gauss);
        if (g == null) {
            throw new IllegalArgumentException("no support of this gauss of course: " + gauss);
        }
        scale = g.scale;
        this.device.write(HMC5883L_RA_CONFIG_B, (byte)(g.register << (HMC5883L_CRB_GAIN_BIT - HMC5883L_CRB_GAIN_LENGTH + 1)));
    }

    private class Gauss {
        double gauss;
        int register;
        double scale;
    }
}
