package net.catchpole.B9.devices.esc;

import java.io.IOException;

public interface ESC {
    void update(int throttle) throws IOException;
}
