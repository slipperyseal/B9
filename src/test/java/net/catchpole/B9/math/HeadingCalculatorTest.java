package net.catchpole.B9.math;

import junit.framework.TestCase;
import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;
import org.junit.Test;

public class HeadingCalculatorTest {
    HeadingCalculator headingCalculator = new HeadingCalculator();

    @Test
    public void testExpectedHeading() {
        Heading newHeading = headingCalculator.getExpectedHeading(
                new Location(-37.5, 145.4),
                new Location(-37.9, 145.8));
        TestCase.assertTrue(Almost.equals(141.6482, 0.0001, newHeading.getDegrees()));

        TestCase.assertTrue(Almost.equals(0.3934, 0.0001, headingCalculator.getSwivel(new Heading(0), newHeading)));
        TestCase.assertTrue(Almost.equals(0.1434, 0.0001, headingCalculator.getSwivel(new Heading(90), newHeading)));
        TestCase.assertTrue(Almost.equals(-0.1065, 0.0001, headingCalculator.getSwivel(new Heading(180), newHeading)));
        TestCase.assertTrue(Almost.equals(-0.3565, 0.0001, headingCalculator.getSwivel(new Heading(270), newHeading)));

        Heading otherHeading = new Heading(324);
        TestCase.assertTrue(Almost.equals(-0.0999, 0.0001, headingCalculator.getSwivel(new Heading(0), otherHeading)));
        TestCase.assertTrue(Almost.equals(-0.3500, 0.0001, headingCalculator.getSwivel(new Heading(90), otherHeading)));
        TestCase.assertTrue(Almost.equals(0.4000, 0.0001, headingCalculator.getSwivel(new Heading(180), otherHeading)));
        TestCase.assertTrue(Almost.equals(0.1500, 0.0001, headingCalculator.getSwivel(new Heading(270), otherHeading)));

        Heading zeroHeading = headingCalculator.getExpectedHeading(
                new Location(-37.5, 145.4),
                new Location(-37.5, 145.4));
        TestCase.assertEquals(0.0d, zeroHeading.getDegrees());

        Heading wrapHeading = headingCalculator.getExpectedHeading(
                new Location(-179.0, -179.0),
                new Location(179.0, 179.0));
    }

    @Test
    public void testGetLocation() {
        TestCase.assertEquals("Location -37.455034 145.400000 0.0 m", headingCalculator.getLocation(new Location(-37.5, 145.4), new Heading(0.0), 5000.0d).toString());
        TestCase.assertEquals("Location -37.455034 145.400247 0.0 m", headingCalculator.getLocation(new Location(-37.5, 145.4), new Heading(0.25), 5000.0d).toString());
        TestCase.assertEquals("Location -37.455036 145.400494 0.0 m", headingCalculator.getLocation(new Location(-37.5, 145.4), new Heading(0.5), 5000.0d).toString());
        TestCase.assertEquals("Location -37.455038 145.400741 0.0 m", headingCalculator.getLocation(new Location(-37.5, 145.4), new Heading(0.75), 5000.0d).toString());

        TestCase.assertTrue(Almost.equals(10.12345, 0.0001, Normalise.degrees(10.1234567)));
        TestCase.assertTrue(Almost.equals(10.12345, 0.0001, Normalise.degrees(-349.8765432)));
        TestCase.assertTrue(Almost.equals(10.12345, 0.0001, Normalise.degrees(3610.1234567)));
        TestCase.assertTrue(Almost.equals(10.12345, 0.0001, Normalise.degrees(-3589.8765432)));
    }

    @Test
    public void testDecimalFraction() {
        TestCase.assertEquals(0.25, headingCalculator.toDecimalFraction(new Heading(90.0).getDegrees()));
        TestCase.assertEquals(0.5, headingCalculator.toDecimalFraction(new Heading(180).getDegrees()));
        TestCase.assertEquals(0.25, headingCalculator.toDecimalFraction(90.0));
        TestCase.assertEquals(0.5, headingCalculator.toDecimalFraction(180));
    }
}