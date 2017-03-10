package net.catchpole.B9.math;

import junit.framework.TestCase;
import net.catchpole.B9.spacial.Location;
import org.junit.Test;

public class DecimalCoordinatesTest {
    @Test
    public void testDecimalCoordinates() {
        Location location = DecimalCoordinates.fromDegreesMinutes("3752.1132","S","14500.2228","E");
        TestCase.assertTrue(Almost.equals(-37.868553, 0.00001, location.getLatitude()));
        TestCase.assertTrue(Almost.equals(145.00371, 0.0001, location.getLongitude()));

        Location otherLocation = DecimalCoordinates.fromDegreesMinutes("3752.1132","N","14500.2228","W");
        TestCase.assertTrue(Almost.equals(37.868553, 0.00001, otherLocation.getLatitude()));
        TestCase.assertTrue(Almost.equals(-145.00371, 0.0001, otherLocation.getLongitude()));
    }
}
