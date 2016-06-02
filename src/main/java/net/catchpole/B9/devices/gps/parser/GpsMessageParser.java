package net.catchpole.B9.devices.gps.parser;

public interface GpsMessageParser<T> {
    public T parse(String[] line);

    public String getKey();
}
