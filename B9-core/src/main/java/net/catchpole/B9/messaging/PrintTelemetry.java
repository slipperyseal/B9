package net.catchpole.B9.messaging;

public class PrintTelemetry implements Telemetry {
    private final Telemetry telemetry;

    public PrintTelemetry() {
        this.telemetry = null;
    }

    public PrintTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void add(Object object) {
        System.out.print(object);
        System.out.print(' ');
        if (telemetry != null) {
            telemetry.add(object);
        }
    }

    public void send() {
        System.out.println();
        if (telemetry != null) {
            telemetry.send();
        }
    }
}
