package net.catchpole.B9.devices.rockblock.webservice;

import net.catchpole.B9.lang.HexString;

public class MTMessage {
    private String imei;
    private String username;
    private String password;
    private byte[] data;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MTMessage{" +
                "imei='" + imei + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", data=" + HexString.toHexString(data) +
                '}';
    }
}
