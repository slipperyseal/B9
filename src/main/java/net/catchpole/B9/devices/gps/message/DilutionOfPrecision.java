package net.catchpole.B9.devices.gps.message;

import java.util.ArrayList;
import java.util.List;

public class DilutionOfPrecision {
    private char fix;
    private List<Integer> pseudorandomNoiseCodes = new ArrayList<Integer>();
    private Double precisionDilution;
    private Double horizontalDilution;
    private Double verticalDilution;

    public int getFix() {
        return fix;
    }

    public void setFix(char fix) {
        this.fix = fix;
    }

    public Double getPrecisionDilution() {
        return precisionDilution;
    }

    public void setPrecisionDilution(Double precisionDilution) {
        this.precisionDilution = precisionDilution;
    }

    public Double getHorizontalDilution() {
        return horizontalDilution;
    }

    public void setHorizontalDilution(Double horizontalDilution) {
        this.horizontalDilution = horizontalDilution;
    }

    public Double getVerticalDilution() {
        return verticalDilution;
    }

    public void setVerticalDilution(Double verticalDilution) {
        this.verticalDilution = verticalDilution;
    }

    public Iterable<Integer> getPseudorandomNoiseCodes() {
        return pseudorandomNoiseCodes;
    }

    public void addPseudorandomNoiseCode(Integer integer) {
        this.pseudorandomNoiseCodes.add(integer);
    }

    @Override
    public String toString() {
        return "DilutionOfPrecision{" +
                "fix=" + fix +
                ", precisionDilution=" + precisionDilution +
                ", horizontalDilution=" + horizontalDilution +
                ", verticalDilution=" + verticalDilution +
                ", pseudorandomNoiseCodes=" + pseudorandomNoiseCodes +
                '}';
    }
}
