package net.catchpole.B9.devices.compass;

public class Heading {
    // currently, a heading (0.0-360.0 degrees) is in the range 0 - 1 - i might change this to the correct 0.0-360.0
    private double degrees;

    public Heading(double degrees) {
        this.degrees = degrees;
    }

    public double getDegrees() {
        return degrees;
    }

    @Override
    public String toString() {
        return "Heading " + String.format( "%.2f", degrees*360.0d );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Heading heading = (Heading) o;

        return Double.compare(heading.degrees, degrees) == 0;

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(degrees);
        return (int) (temp ^ (temp >>> 32));
    }
}
