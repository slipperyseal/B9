package net.catchpole.B9.devices.compass;

import net.catchpole.B9.spacial.Heading;

public class OffsetCompass implements Compass {
    private Compass compass;
    private double offset;
    private double lastActual;

    public OffsetCompass(Compass compass) {
        this.compass = compass;
    }

    public OffsetCompass(Compass compass, double offset) {
        this.compass = compass;
        this.offset = offset;
    }

    public void setOffset(Heading offsetHeading) {
        this.offset = offsetHeading.getDegrees();
    }

    public Heading getOffset() {
        return new Heading(normalize(this.offset));
    }

    public void setNorth() {
        this.offset = 0.0D - this.lastActual;
    }

    @Override
    public Heading getHeading() {
        Heading heading = compass.getHeading();
        if (heading != null) {
            return new Heading(normalize((this.lastActual = heading.getDegrees()) + offset));
        }
        return null;
    }

    private double normalize(double heading) {
        while (heading < 0.0D) {
            heading += 360.0D;
        }
        while (heading > 360.0D) {
            heading -= 360.0D;
        }
        return heading;
    }
}
