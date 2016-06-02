package net.catchpole.B9.devices.rockblock.message;

public class Reception {
    private int reception;

    public Reception(String[] data) {
        this.reception = Integer.parseInt(data[0]);
    }

    public int getReception() {
        return reception;
    }

    @Override
    public String toString() {
        return "Reception{" +
                "reception=" + reception +
                '}';
    }
}
