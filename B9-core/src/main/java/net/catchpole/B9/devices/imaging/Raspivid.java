package net.catchpole.B9.devices.imaging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Raspivid {
    private Integer width;
    private Integer height;
    private Integer bitrate;

    public Raspivid(Integer width, Integer height, Integer bitrate) {
        this.width = width;
        this.height = height;
        this.bitrate = bitrate;
    }

    //raspivid -t 30000 -b 1000000 -w 1280 -h 720 -o test.h264
    public synchronized void shoot(File file, int millis) {
        try {
            List<String> args = new ArrayList<String>();
            args.add("raspivid");
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
            if (bitrate != null) {
                args.add("-b");
                args.add(Integer.toString(bitrate));
            }
            args.add("-t");
            args.add(Integer.toString(millis));
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shoot(final File file, final int millis, final Runnable runnable) {
        new Thread() {
            public void run() {
                shoot(file, millis);
                if (runnable != null) {
                    runnable.run();
                }
            }
        }.start();
    }
}
