package net.catchpole.B9.math;

public class Normalise {
    public static double fraction(double value) {
        int intValue = (int)value;
        value -= intValue;
        return value < 0 ? value+1.0d : value;
    }

    public static double degrees(double value) {
        return value >= 0 ? (value < 360.0 ? value : value % 360.0d) : 360.0d-((0.0d-value)%360.0d);
    }
}
