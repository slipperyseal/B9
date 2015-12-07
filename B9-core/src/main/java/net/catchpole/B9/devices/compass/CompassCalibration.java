package net.catchpole.B9.devices.compass;

import net.catchpole.B9.spacial.Heading;

import java.util.ArrayList;
import java.util.List;

public class CompassCalibration implements HeadingFilter {
    private List<Double> calibration = new ArrayList<Double>();

    public List<Double> getCalibration() {
        return calibration;
    }

    public void setCalibration(List<Double> calibration) {
        this.calibration = calibration;
    }

    @Override
    public Heading filter(Heading heading) {
        return null;
    }

    class Range {
        double start;
        double stop;
    }
}
