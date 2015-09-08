package net.catchpole.B9.math;

public class WeightedAverage {
    private final double[] data;
    private int len;

    public WeightedAverage(int size) {
        this.data = new double[size];
        this.len = 0;
    }

    public void add(double value) {
        if (len < data.length) {
            len++;
        }
        for (int x=1;x<len;x++) {
            data[len-x] = data[len-x-1];
        }
        data[0] = value;
    }

    public double calculate() {
        if (this.len == 0) {
            return 0;
        }
        if (this.len == 1) {
            return data[0];
        }
        double total = 0;
        double divide = 0;
        double weightSection = 1.0/this.len;
        for (int x=0;x<this.len;x++) {
            double weight = 1.0-((weightSection)*x);
            total += data[x] * weight;
            divide += weight;
        }
        return total/divide;
    }
}
