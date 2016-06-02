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
    }

    @Test
    public void isWithin() {
        Location home = new Location(-37.321353, 145.5164683);
        TestCase.assertFalse(distanceCalculator.isWithinDistance(new Location(-37.322534, 145.527165), home, 1.0));
        TestCase.assertTrue(distanceCalculator.isWithinDistance(new Location(-37.320544, 145.517042), home, 1.0));
    }
}
