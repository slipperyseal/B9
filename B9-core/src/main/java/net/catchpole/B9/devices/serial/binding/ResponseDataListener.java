package net.catchpole.B9.devices.serial.binding;

import net.catchpole.B9.devices.serial.DataListener;

import java.io.IOException;

public class ResponseDataListener implements DataListener {
    private final long timeout = 100000;
    private final byte[] buffer = new byte[8192];
    private int length = 0;

    @Override
    public synchronized void receive(byte[] data, int len) {
        if (length + len > buffer.length) {
            System.out.println("ResponseDataListener overflow");
            return;
        }

//        for (int x=0;x<len;x++) {
//            System.out.println(x + " " + data[x]);
//        }

        System.arraycopy(data,0,this.buffer,this.length,len);
        this.length += len;
        this.notifyAll();
    }

    public synchronized String readString() throws IOException {
        long startTime = System.currentTimeMillis();
        String string = getString();
        if (string != null) {
            return string;
        }
        for (;;) {
            try {
                this.wait(timeout);
                string = getString();
                if (string != null) {
                    return string;
                }
                if (System.currentTimeMillis() > startTime+timeout) {
                    throw new IOException("timeout");
                }
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
        }
    }

    public synchronized byte[] readBytesFully(int len) throws IOException {
        byte[] data = new byte[len];
        if (readByteArray(data)) {
            return data;
        }
        for (;;) {
            try {
                this.wait(timeout);
                if (!readByteArray(data)) {
                    throw new IOException("timeout");
                }
                return data;
            } catch (InterruptedException e) {
                throw new IOException("InterruptedException");
            }
        }
    }

    private boolean readByteArray(byte[] data) {
        if (length >= data.length) {
            System.arraycopy(buffer,0,data,0,data.length);
            copyBack(data.length, length-data.length);
            return true;
        }
        return false;
    }

    private String getString() {
        for (int x=0;x<length;x++) {
            if (buffer[x] == 10) {
                String string = new String(buffer, 0, x).trim();
                copyBack(x+1, length-x-1);
                return string;
            }
        }
        return null;
    }

    private void copyBack(int offset, int len) {
        for (int y=0;y<len;y++) {
            buffer[y] = buffer[offset++];
        }
        length = len;
    }
}
