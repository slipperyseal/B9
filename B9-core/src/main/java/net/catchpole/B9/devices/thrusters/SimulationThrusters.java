package net.catchpole.B9.devices.thrusters;

import net.catchpole.B9.devices.gps.SimulationGps;
import net.catchpole.B9.math.DistanceCalculator;
import net.catchpole.B9.math.HeadingCalculator;
import net.catchpole.B9.math.Normalise;
import net.catchpole.B9.spacial.Heading;

import java.util.Random;

public class SimulationThrusters implements Thrusters {
    private HeadingCalculator headingCalculator = new HeadingCalculator();
    private DistanceCalculator distanceCalculator = new DistanceCalculator();

    private final Random random = new Random();
    private final SimulationGps simulationGps;
    private final double kilometersPerSecond;
    private final int errorDegrees;

    private double left;
    private double right;

    public SimulationThrusters(SimulationGps simulationGps, double kilometersPerSecond, int errorDegrees) {
        this.simulationGps = simulationGps;
        this.kilometersPerSecond = kilometersPerSecond;
        this.errorDegrees = errorDegrees;
    }

    public void update(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public void adviseNewHeading(Heading heading) {
        double error = errorDegrees == 0 ? 0 : random.nextInt(errorDegrees) - (errorDegrees/2);
        simulationGps.setHeading(new Heading(Normalise.degrees(heading.getDegrees() + error)));
    }

    public void oneSecondTick() {
        if (left != 0.0 && right != 0.0) {
            double kps = kilometersPerSecond * ((left + right) / 2.0d);
            simulationGps.setLocation(
                    headingCalculator.getLocation(simulationGps.getLocation(), simulationGps.getHeading(),
                            distanceCalculator.kilometersToDegrees(kps))
            );
        }
    }
}