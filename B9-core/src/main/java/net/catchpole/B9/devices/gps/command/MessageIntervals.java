package net.catchpole.B9.devices.gps.command;

public class MessageIntervals implements GpsCommand {
    private int[] values = new int[19];

    public void set(int index, int value) {
        values[index] = value;
    }

    public void setGPGLLInterval(int interval) {
        set(0, interval);
    }

    public void setGPRMCInterval(int interval) {
        set(1, interval);
    }

    public void setGPVTGInterval(int interval) {
        set(2, interval);
    }

    public void setGPGGAInterval(int interval) {
        set(3, interval);
    }

    public void setGPGSAInterval(int interval) {
        set(4, interval);
    }

    public void setGPGSVInterval(int interval) {
        set(5, interval);
    }

    public String getSentence() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("$PMTK314");
        for (int x=0;x<values.length;x++) {
            stringBuilder.append(',');
            stringBuilder.append(Integer.toString(values[x]));
        }
        return stringBuilder.toString();
    }
}
