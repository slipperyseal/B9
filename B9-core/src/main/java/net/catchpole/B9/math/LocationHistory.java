package net.catchpole.B9.math;

import net.catchpole.B9.spacial.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LocationHistory implements Iterable<Location> {
    private List<Location> list = new ArrayList<Location>();
    private final int capasity;

    public LocationHistory(int capasity) {
        this.capasity = capasity;
    }

    public void addLocation(Location location) {
        if (list.size() >= capasity) {
            list.remove(0);
        }
        list.add(location);
    }

    public Iterator<Location> iterator() {
        return list.iterator();
    }
}
