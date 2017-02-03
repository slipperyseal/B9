package net.catchpole.B9.devices.serial;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SocketSerialPort implements SerialPort {
    private String host;
    private int port;
    private Thread thread;

    public SocketSerialPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public SerialConnection openConnection(int baud) throws IOException {
        final Socket socket = new Socket(InetAddress.getByName(host), port);
        final OutputStream outputStream = socket.getOutputStream();
        final SerialConnection serialConnection = new SerialConnection() {
            @Override
            public void write(byte[] data) throws IOException {
                outputStream.write(data);
                outputStream.flush();
            }

            @Override
            public void close() throws IOException {
                try {
                    thread.interrupt();
                    socket.close();
                } catch (IOException e) {
                }
            }

            public InputStream getInputStream() throws IOException {
                return socket.getInputStream();
            }

            @Override
            public void setDataListener(final DataListener dataListener) {
                SocketSerialPort.this.thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            final InputStream inputStream = socket.getInputStream();
                            final byte[] data = new byte[512];
                            for (; ; ) {
                                int len = inputStream.read(data);
                                if (len == -1) {
                                    throw new EOFException();
                                }
                                dataListener.receive(data, len);
                            }
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            try {
                                close();
                            } catch (IOException ioeClose) {
                                ioeClose.printStackTrace();
                            }
                            return;
                        }
                    }
                };
                thread.start();
            }
        };
        return serialConnection;
    }
}

