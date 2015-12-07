package net.catchpole.B9.devices.gps.command;

import net.catchpole.B9.devices.gps.Sentence;

public class GpsCommandSender {
    private final Sentence sentence = new Sentence();
    private final LineWriter lineWriter;

    public GpsCommandSender(LineWriter lineWriter) {
        this.lineWriter = lineWriter;
    }

    public void send(GpsCommand gpsCommand) {
        lineWriter.writeLine(sentence.addChecksumAndCrlf(gpsCommand.getSentence()));
    }

    public void delay() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
    }
}
