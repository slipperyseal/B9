package net.catchpole.B9.math;

import junit.framework.TestCase;
import org.junit.Test;

public class WeightedAverageTest {
    @Test
    public void testWeightedAverage() {
        double[] results = new double[] {
            1.0, 1.6, 2.3, 3.0, 3.6, 4.3, 4.9, 5.6, 6.3, 7.0, 8.0, 8.9, 9.9, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0
        };

        WeightedAverage weightedAverage = new WeightedAverage(10);
        for (int x=0;x<20;x++) {
            weightedAverage.add(x+1);
            TestCase.assertTrue(Almost.equals(results[x], 0.1, weightedAverage.calculate()));
        }
    }
}
