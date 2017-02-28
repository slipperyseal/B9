package net.catchpole.B9.math;

import junit.framework.TestCase;
import net.catchpole.B9.spacial.Location;
import org.junit.Test;

public class DistanceCalculatorTest {
    private DistanceCalculator distanceCalculator = new DistanceCalculator();

    @Test
    public void testDistance() {
        TestCase.assertTrue(Almost.equals(0.1414, 0.0001, distanceCalculator.degreesDistance(new Location(-37.5, 145.4), new Location(-37.6, 145.5))));
        TestCase.assertTrue(Almost.equals(0.4242, 0.0001, distanceCalculator.degreesDistance(new Location(-37.5, 145.4), new Location(-37.2, 145.1))));
        TestCase.assertTrue(Almost.equals(2.8284, 0.0001, distanceCalculator.degreesDistance(new Location(-179.0, -179.0), new Location(179.0, 179.0))));
    }

    @Test
    public void testMetersToDegrees() {
        TestCase.assertTrue(Almost.equals(2.0D, 0.0001, distanceCalculator.metersToDegreesLatitude(111194.92664455873 * 2, new Location(-37.5, 145.4))));
        TestCase.assertTrue(Almost.equals(1.0D, 0.0001, distanceCalculator.metersToDegreesLatitude(111194.92664455873, new Location(-37.5, 145.4))));
        TestCase.assertTrue(Almost.equals(0.5D, 0.0001, distanceCalculator.metersToDegreesLatitude(111194.92664455873 / 2, new Location(-37.5, 145.4))));

        TestCase.assertTrue(Almost.equals(2.0D, 0.0001, distanceCalculator.metersToDegreesLongitude(88216.45152563702 * 2, new Location(-37.5, 145.4))));
        TestCase.assertTrue(Almost.equals(1.0D, 0.0001, distanceCalculator.metersToDegreesLongitude(88216.45152563702, new Location(-37.5, 145.4))));
        TestCase.assertTrue(Almost.equals(0.5D, 0.0001, distanceCalculator.metersToDegreesLongitude(88216.45152563702 / 2, new Location(-37.5, 145.4))));
    }

    @Test
    public void testIsWithin() {
        Location home = new Location(-37.321353, 145.5164683);
        TestCase.assertFalse(distanceCalculator.isWithinDistance(new Location(-37.322534, 145.529165), home, 1000));
        TestCase.assertTrue(distanceCalculator.isWithinDistance(new Location(-37.320544, 145.517042), home, 1000));
    }

    @Test
    public void testDistanceMeters() {
        TestCase.assertEquals(76541, (int) distanceCalculator.distanceMeters(new Location(-37.8136428, 144.9558319), new Location(-38.5012549, 144.9152961)));
    }
}
