package net.catchpole.B9.devices.gps.command;

public class FixUpdateRate implements GpsCommand {
    private int milliseconds;

    public FixUpdateRate(int milliseconds) {
        this.milliseconds = milliseconds;
    }
    @Override
    public String getSentence() {
        return "$PMTK300," + milliseconds + ",0,0,0,0";
    }
}
