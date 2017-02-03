package net.catchpole.B9.devices.serial;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// want to use that serial port remotely? connect it to a socket
public class SocketListenerSerialPort implements SerialPort {
    private final SerialPort serialPort;
    private final int baud;
    private final ServerSocket serverSocket;

    public SocketListenerSerialPort(SerialPort serialPort, int port, int baud) throws IOException {
        this.baud = baud;
        this.serialPort = serialPort;
        this.serverSocket = new ServerSocket(port);
    }

    public void accept() {
        for (;;) {
            final SerialConnection[] serialConnection = new SerialConnection[1];
            try {
                final Socket socket = this.serverSocket.accept();
                final OutputStream outputStream = socket.getOutputStream();
                serialConnection[0] = openConnection(baud);
                serialConnection[0].setDataListener(new DataListener() {
                    @Override
                    public void receive(byte[] data, int len) {
                        try {
                            outputStream.write(data, 0, len);
                            outputStream.flush();
                        } catch (IOException ioe) {
                            try {
                                serialConnection[0].close();
                            } catch (IOException ioeClose) {
                                ioeClose.printStackTrace();
                            }
                        }
                    }
                });

                final InputStream inputStream = socket.getInputStream();
                final byte[] data = new byte[512];
                for (;;) {
                    int len = inputStream.read(data);
                    if (len == -1) {
                        throw new EOFException();
                    }
                    if (len == data.length) {
                        serialConnection[0].write(data);
                    } else {
                        byte[] write = new byte[len];
                        System.arraycopy(data, 0, write, 0, len);
                        serialConnection[0].write(write);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    serialConnection[0].close();
                } catch (IOException ioeClose) {
                    ioeClose.printStackTrace();
                }
            }
        }
    }

    @Override
    public SerialConnection openConnection(int baud) throws IOException, InterruptedException {
        return serialPort.openConnection(baud);
    }
}
