package net.catchpole.B9.devices.gps.command;

public class NmeaUpdateRate implements GpsCommand {
    private int milliseconds;

    public NmeaUpdateRate(int milliseconds) {
        this.milliseconds = milliseconds;
    }
    @Override
    public String getSentence() {
        return "$PMTK220," + milliseconds;
    }
}
