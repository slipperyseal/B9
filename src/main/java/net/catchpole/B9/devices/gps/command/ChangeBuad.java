package net.catchpole.B9.devices.gps.command;

public class ChangeBuad implements GpsCommand {
    private int baud;

    public ChangeBuad(int baud) {
        this.baud = baud;
    }

    public ChangeBuad() {
    }

    public void setBaud(int baud) {
        this.baud = baud;
    }

    public String getSentence() {
        return "$PMTK251," + baud;
    }
}
