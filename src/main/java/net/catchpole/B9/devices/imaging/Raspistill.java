package net.catchpole.B9.devices.imaging;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// without direct access to the camera module, we can simply call the raspistill command
// not awesome. but better than nothing.
public class Raspistill {
    private Integer width;
    private Integer height;
    private Integer quality;

    public Raspistill(Integer width, Integer height, Integer quality) {
        this.width = width;
        this.height = height;
        this.quality = quality;
    }

    public void snap(File file, boolean wait) throws IOException, InterruptedException {
        List<String> args = new ArrayList<String>();
        args.add("raspistill");
        args.add("-o");
        args.add(file.getAbsolutePath());
        if (width != null) {
            args.add("-w");
            args.add(Integer.toString(width));
        }
        if (height != null) {
            args.add("-h");
            args.add(Integer.toString(height));
        }
        if (quality != null) {
            args.add("-q");
            args.add(Integer.toString(quality));
        }
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = processBuilder.start();
        if (wait) {
            process.waitFor();
        }
    }
}
