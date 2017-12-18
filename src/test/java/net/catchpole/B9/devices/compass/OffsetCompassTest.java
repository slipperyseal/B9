package net.catchpole.B9.devices.compass;

import junit.framework.TestCase;
import net.catchpole.B9.spacial.Heading;
import org.junit.Test;

public class OffsetCompassTest {
    @Test
    public void testOffsetCompass() {
        final Heading[] heading = new Heading[1];
        heading[0] = new Heading(20);

        Compass compass = new Compass() {
            @Override
            public Heading getHeading() {
                return heading[0];
            }
        };

        OffsetCompass offsetCompass = new OffsetCompass(compass);
        TestCase.assertEquals(20.0, offsetCompass.getHeading().getDegrees());

        offsetCompass.setNorth();

        TestCase.assertEquals(0.0, offsetCompass.getHeading().getDegrees());
        TestCase.assertEquals(340.0, offsetCompass.getOffset().getDegrees());
    }
}
