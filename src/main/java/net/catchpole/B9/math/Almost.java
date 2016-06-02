package net.catchpole.B9.math;

public class Almost {
    // used to check an "almost" value because floating point math
    public static boolean equals(double expected, double precision, double value) {
        return value >= (expected-precision) && value <= (expected+precision);
    }
}
