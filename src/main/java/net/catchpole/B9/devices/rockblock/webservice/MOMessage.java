package net.catchpole.B9.devices.rockblock.webservice;

import net.catchpole.B9.lang.HexString;

public class MOMessage {
    private String imei;
    private String momsn;
    private String transmitTime;
    private String iridiumLatitude;
    private String iridiumLongitude;
    private String iridiumCep;
    private byte[] data;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMomsn() {
        return momsn;
    }

    public void setMomsn(String momsn) {
        this.momsn = momsn;
    }

    public String getTransmitTime() {
        return transmitTime;
    }

    public void setTransmitTime(String transmitTime) {
        this.transmitTime = transmitTime;
    }

    public String getIridiumLatitude() {
        return iridiumLatitude;
    }

    public void setIridiumLatitude(String iridiumLatitude) {
        this.iridiumLatitude = iridiumLatitude;
    }

    public String getIridiumLongitude() {
        return iridiumLongitude;
    }

    public void setIridiumLongitude(String iridiumLongitude) {
        this.iridiumLongitude = iridiumLongitude;
    }

    public String getIridiumCep() {
        return iridiumCep;
    }

    public void setIridiumCep(String iridiumCep) {
        this.iridiumCep = iridiumCep;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MOMessage{" +
                "imei='" + imei + '\'' +
                ", momsn='" + momsn + '\'' +
                ", transmitTime='" + transmitTime + '\'' +
                ", iridiumLatitude='" + iridiumLatitude + '\'' +
                ", iridiumLongitude='" + iridiumLongitude + '\'' +
                ", iridiumCep='" + iridiumCep + '\'' +
                ", data=" + HexString.toHexString(data) +
                '}';
    }
}
