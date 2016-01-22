package net.catchpole.B9.devices.esc;

public class BlueESCData {
    private static final float THERMISTORNOMINAL = 10000; // resistance at 25 degrees C
    private static final float TEMPERATURENOMINAL = 25; // temp. for nominal resistance (almost always 25 C)
    private static final float BCOEFFICIENT = 3900; // The beta coefficient of the thermistor (usually 3000-4000)
    private static final float SERIESRESISTOR = 3300; // the value of the 'other' resistor

    private int pulse;
    private double voltage;
    private double temperature;
    private double current;
    private int identifier;

    // construct with normalized values
    public BlueESCData(int pulse, double voltage, double temperature, double current) {
        this.pulse = pulse;
        this.voltage = voltage;
        this.temperature = temperature;
        this.current = current;
        this.identifier = 0xab;
    }

    // construct with raw BlueESC I2C values
    public BlueESCData(int pulse, int voltage, int temperature, int current, int identifier) {
        this.pulse = pulse;
        this.voltage = ((double)voltage)/65536.0f * 5.0f * 6.45f;
        this.temperature = calculateTemperature(temperature);
        this.current = (((double)current)-32767.0f)/65535.0f * 5.0f * 14.706f;
        this.identifier = identifier;
    }

    private double calculateTemperature(float raw) {
        double resistance = SERIESRESISTOR/(65535/raw-1);
        double steinhart = resistance / THERMISTORNOMINAL;  // (R/Ro)
        steinhart = Math.log(steinhart);             // ln(R/Ro)
        steinhart /= BCOEFFICIENT;                   // 1/B * ln(R/Ro)
        steinhart += 1.0 / (TEMPERATURENOMINAL + 273.15); // + (1/To)
        steinhart = 1.0 / steinhart;                 // invert
        steinhart -= 273.15;                         // convert to C
        return steinhart;
    }

    public int getPulse() {
        return pulse;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getCurrent() {
        return current;
    }

    public int getIdentifier() {
        return identifier;
    }

    public boolean isAlive() {
        return identifier == 0xab;
    }

    @Override
    public String toString() {
        return "BlueESCData " +
                "pulse=" + pulse +
                ", voltage=" + String.format( "%.2f", voltage ) +
                ", temperature=" + String.format( "%.2f", temperature ) +
                ", current=" + String.format( "%.2f", current ) +
                ", isAlive=" + isAlive() +
                (isAlive() ? "" : " identifier: " + identifier);
    }
}
