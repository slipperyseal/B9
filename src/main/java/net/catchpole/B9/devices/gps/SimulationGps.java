package net.catchpole.B9.devices.gps;

import net.catchpole.B9.devices.compass.Compass;
import net.catchpole.B9.spacial.Heading;
import net.catchpole.B9.spacial.Location;

public class SimulationGps implements Gps, Compass {
    private Location location;
    private Heading heading;

    public Location getLocation() {
        return location;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }
}
