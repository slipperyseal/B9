package net.catchpole.B9.devices.gps;

import net.catchpole.B9.devices.compass.Compass;
import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;

public class SimulationGps implements Gps, Compass, Speedometer {
    private Location location;
    private Heading heading;
    private double velocity;

    public Location getLocation() {
        return location;
    }

    public Heading getHeading() {
        return heading;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}
