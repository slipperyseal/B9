package net.catchpole.B9.devices.compass;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

// a work in progress. im getting crazy results from it :/ awkward
public class HMC5883LCompass implements Compass {
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

    private final I2CBus bus;
    private final I2CDevice device;
    private final byte[] bytes = new byte[32];
    private double scale;

    public HMC5883LCompass() throws Exception {
        this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        this.device = bus.getDevice(HMC5883L_ADDRESS);

        this.device.write(HMC5883L_RA_CONFIG_A,
                (byte)( (HMC5883L_AVERAGING_8 << (HMC5883L_CRA_AVERAGE_BIT - HMC5883L_CRA_AVERAGE_LENGTH + 1)) |
                        (HMC5883L_RATE_15     << (HMC5883L_CRA_RATE_BIT - HMC5883L_CRA_RATE_LENGTH + 1)) |
                        (HMC5883L_BIAS_NORMAL << (HMC5883L_CRA_BIAS_BIT - HMC5883L_CRA_BIAS_LENGTH + 1)) ));
        setScale(1.3);
//        this.device.write(HMC5883L_RA_CONFIG_B,
//                (byte)(HMC5883L_GAIN_1090 << (HMC5883L_CRB_GAIN_BIT - HMC5883L_CRB_GAIN_LENGTH + 1)) );
        this.device.write(HMC5883L_RA_MODE,
                (byte) (HMC5883L_MODE_CONTINUOUS << (HMC5883L_MODEREG_BIT - HMC5883L_MODEREG_LENGTH + 1)));
    }

    public boolean test() throws Exception {
        return device.read(HMC5883L_RA_ID_A, bytes, 0, 3) == 3 && bytes[0] == 'H' && bytes[1] == '4' && bytes[2] == '3';
    }

    public Heading getHeading() {
        try {
            int r;
            if ((r=device.read(HMC5883L_RA_DATAX_H, bytes, 0, 6)) == 6) {
                short rx = (short)(((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff));
                short rz = (short)(((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff));
                short ry = (short)(((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff));

                double x = rx * scale;
                double y = ry * scale;
                double z = rz * scale;

                double heading = Math.atan2(y,x) * (180 / Math.PI) + 180; // angle in degrees

                System.out.println(heading + "\t| rx = " + rx + "\try = " + ry + "\trz =  " + rz + "\t| x =" + (int)x + "\ty = " + (int)y + "\tz = " + (int)z);

                return new Heading(heading/360.0);
            } else {
                System.out.println("ERRRR: " + r);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return new Heading(0.0);
    }

    public void setScale(double gauss) throws IOException {
        int register;
        if(gauss == 0.88) {
            register = 0x00;
            scale = 0.73;
        } else if(gauss == 1.3) {
            register = 0x01;
            scale = 0.92;
        } else if(gauss == 1.9) {
            register = 0x02;
            scale = 1.22;
        } else if(gauss == 2.5) {
            register = 0x03;
            scale = 1.52;
        } else if(gauss == 4.0) {
            register = 0x04;
            scale = 2.27;
        } else if(gauss == 4.7) {
            register = 0x05;
            scale = 2.56;
        } else if(gauss == 5.6) {
            register = 0x06;
            scale = 3.03;
        } else if(gauss == 8.1) {
            register = 0x07;
            scale = 4.35;
        } else {
            throw new IllegalArgumentException("no support of this gauss of course: " + gauss);
        }
        this.device.write(HMC5883L_RA_CONFIG_B, (byte)(register << (HMC5883L_CRB_GAIN_BIT - HMC5883L_CRB_GAIN_LENGTH + 1)));
    }

    public static void main(String[] args) throws Exception {
        HMC5883LCompass hmc5883LCompass = new HMC5883LCompass();
        for (;;) {
            hmc5883LCompass.getHeading();
            Thread.sleep(200);
        }
    }
}
