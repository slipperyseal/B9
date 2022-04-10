package net.catchpole.B9.math;

import junit.framework.TestCase;
import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;
import org.junit.Test;

public class GeoTest {
    @Test
    public void testDistance() {
        TestCase.assertTrue(Almost.equals(0.1414, 0.0001, Geo.degreesDistance(new Location(-37.5, 145.4), new Location(-37.6, 145.5))));
        TestCase.assertTrue(Almost.equals(0.4242, 0.0001, Geo.degreesDistance(new Location(-37.5, 145.4), new Location(-37.2, 145.1))));
        TestCase.assertTrue(Almost.equals(2.8284, 0.0001, Geo.degreesDistance(new Location(-179.0, -179.0), new Location(179.0, 179.0))));
    }

    @Test
    public void testMetersToDegrees() {
        TestCase.assertTrue(Almost.equals(2.0D, 0.0001, Geo.metersToDegreesLatitude(111194.92664455873 * 2, new Location(-37.5, 145.4))));
        TestCase.assertTrue(Almost.equals(1.0D, 0.0001, Geo.metersToDegreesLatitude(111194.92664455873, new Location(-37.5, 145.4))));
        TestCase.assertTrue(Almost.equals(0.5D, 0.0001, Geo.metersToDegreesLatitude(111194.92664455873 / 2, new Location(-37.5, 145.4))));

        TestCase.assertTrue(Almost.equals(2.0D, 0.0001, Geo.metersToDegreesLongitude(88216.45152563702 * 2, new Location(-37.5, 145.4))));
        TestCase.assertTrue(Almost.equals(1.0D, 0.0001, Geo.metersToDegreesLongitude(88216.45152563702, new Location(-37.5, 145.4))));
        TestCase.assertTrue(Almost.equals(0.5D, 0.0001, Geo.metersToDegreesLongitude(88216.45152563702 / 2, new Location(-37.5, 145.4))));
    }

    @Test
    public void testIsWithin() {
        Location home = new Location(-37.321353, 145.5164683);
        TestCase.assertFalse(Geo.isWithinDistance(new Location(-37.322534, 145.529165), home, 1000, false));
        TestCase.assertTrue(Geo.isWithinDistance(new Location(-37.320544, 145.517042), home, 1000, false));

        System.out.println(Geo.metersToDegreesLatitude(1000.0d, new Location(-37.5, 145.4)));
        System.out.println(Geo.metersToDegreesLongitude(1000.0d, new Location(-37.5, 145.4)));

        float lon = -37.8136428f - (-37.8136428f + 0.008993216059187306f);
        float lat = 144.9558319f - 144.9558319f;
        if (lon < 0) lon = 0-lon;
        if (lat < 0) lat = 0-lat;
        float mul = (130000.0f/1169.3573f)*1000.0f;
        System.out.println (mul);
        System.out.println ((lon > lat ? lon : lat) * 152016.45);
        System.out.println ((lon > lat ? lon : lat) * 110000);

    }

    @Test
    public void testDistanceMeters() {
        TestCase.assertEquals(76541, (int) Geo.distanceMeters(new Location(-37.8136428, 144.9558319), new Location(-38.5012549, 144.9152961, 2000), false));
        TestCase.assertEquals(76567, (int) Geo.distanceMeters(new Location(-37.8136428, 144.9558319), new Location(-38.5012549, 144.9152961, 2000), true));
    }

    @Test
    public void testExpectedHeading() {
        Heading newHeading = Geo.getExpectedHeading(
                new Location(-37.5, 145.4),
                new Location(-37.9, 145.8));
        TestCase.assertTrue(Almost.equals(141.6482, 0.0001, newHeading.getDegrees()));

        TestCase.assertTrue(Almost.equals(0.3934, 0.0001, Geo.getSwivel(new Heading(0), newHeading)));
        TestCase.assertTrue(Almost.equals(0.1434, 0.0001, Geo.getSwivel(new Heading(90), newHeading)));
        TestCase.assertTrue(Almost.equals(-0.1065, 0.0001, Geo.getSwivel(new Heading(180), newHeading)));
        TestCase.assertTrue(Almost.equals(-0.3565, 0.0001, Geo.getSwivel(new Heading(270), newHeading)));

        Heading otherHeading = new Heading(324);
        TestCase.assertTrue(Almost.equals(-0.0999, 0.0001, Geo.getSwivel(new Heading(0), otherHeading)));
        TestCase.assertTrue(Almost.equals(-0.3500, 0.0001, Geo.getSwivel(new Heading(90), otherHeading)));
        TestCase.assertTrue(Almost.equals(0.4000, 0.0001, Geo.getSwivel(new Heading(180), otherHeading)));
        TestCase.assertTrue(Almost.equals(0.1500, 0.0001, Geo.getSwivel(new Heading(270), otherHeading)));

        Heading zeroHeading = Geo.getExpectedHeading(
                new Location(-37.5, 145.4),
                new Location(-37.5, 145.4));
        TestCase.assertEquals(0.0d, zeroHeading.getDegrees());

        Heading wrapHeading = Geo.getExpectedHeading(
                new Location(-179.0, -179.0),
                new Location(179.0, 179.0));
    }

    @Test
    public void testGetLocation() {
        TestCase.assertEquals("Location -37.455034 145.400000 0.0 m", Geo.getLocation(new Location(-37.5, 145.4), new Heading(0.0), 5000.0d).toString());
        TestCase.assertEquals("Location -37.455034 145.400247 0.0 m", Geo.getLocation(new Location(-37.5, 145.4), new Heading(0.25), 5000.0d).toString());
        TestCase.assertEquals("Location -37.455036 145.400494 0.0 m", Geo.getLocation(new Location(-37.5, 145.4), new Heading(0.5), 5000.0d).toString());
        TestCase.assertEquals("Location -37.455038 145.400741 0.0 m", Geo.getLocation(new Location(-37.5, 145.4), new Heading(0.75), 5000.0d).toString());

        TestCase.assertTrue(Almost.equals(10.12345, 0.0001, Normalise.degrees(10.1234567)));
        TestCase.assertTrue(Almost.equals(10.12345, 0.0001, Normalise.degrees(-349.8765432)));
        TestCase.assertTrue(Almost.equals(10.12345, 0.0001, Normalise.degrees(3610.1234567)));
        TestCase.assertTrue(Almost.equals(10.12345, 0.0001, Normalise.degrees(-3589.8765432)));
    }

    @Test
    public void testDecimalFraction() {
        TestCase.assertEquals(0.25, Geo.toDecimalFraction(new Heading(90.0).getDegrees()));
        TestCase.assertEquals(0.5, Geo.toDecimalFraction(new Heading(180).getDegrees()));
        TestCase.assertEquals(0.25, Geo.toDecimalFraction(90.0));
        TestCase.assertEquals(0.5, Geo.toDecimalFraction(180));
    }    
}
