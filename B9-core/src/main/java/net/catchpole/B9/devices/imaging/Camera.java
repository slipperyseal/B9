package net.catchpole.B9.devices.imaging;

import java.io.File;

public interface Camera {
    void snap(File file);

    void snap(File file, Runnable runnable);
}
