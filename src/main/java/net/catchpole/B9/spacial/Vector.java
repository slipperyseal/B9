package net.catchpole.B9.spacial;

public class Vector {
    private Heading heading;
    private double velocity;

    public Vector(Heading heading, double velocity) {
        this.heading = heading;
        this.velocity = velocity;
    }

    public Heading getHeading() {
        return heading;
    }

    public double getVelocity() {
        return velocity;
    }
}
