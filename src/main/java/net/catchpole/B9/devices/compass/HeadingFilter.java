package net.catchpole.B9.devices.compass;

import net.catchpole.B9.spacial.Heading;

public interface HeadingFilter {
    Heading filter(Heading heading);
}
