package net.catchpole.B9.devices.serial;

import jtermios.JTermios;
import jtermios.Termios;
import jtermios.TimeVal;

import java.io.IOException;

import static jtermios.JTermios.*;

public class TermiosSerialConnection implements SerialConnection {
    private final int fd;
    private final TimeVal timeVal;
    private final JTermios.FDSet fdSet = newFDSet();
    private Thread thread = new Thread(new Reader());
    private DataListener dataListener;

    public TermiosSerialConnection(String port, int baud, DataListener dataListener) throws IOException {
        this.dataListener = dataListener;
        this.fd = open(port == null ? "/dev/tty.wchusbserial1410" : port, O_RDWR | O_NOCTTY | O_NONBLOCK);
        if (this.fd == -1) {
            throw new IOException("unable to open " + port + " error " + fd);
        }
        fcntl(fd, F_SETFL, 0);

        Termios termios = new Termios();
        tcgetattr(fd, termios);
        termios.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
        termios.c_cflag |= (CLOCAL | CREAD);
        termios.c_cflag &= ~PARENB;
        termios.c_cflag |= CSTOPB;
        termios.c_cflag &= ~CSIZE;
        termios.c_cflag |= CS8;
        termios.c_oflag &= ~OPOST;
        termios.c_iflag &= ~INPCK;
        termios.c_iflag &= ~(IXON | IXOFF | IXANY);
        termios.c_cc[VMIN] = 0;
        termios.c_cc[VTIME] = 10;

        cfsetispeed(termios, baud);
        cfsetospeed(termios, baud);

        tcsetattr(fd, TCSANOW, termios);

        tcflush(fd, TCIOFLUSH);

        FD_ZERO(fdSet);
        FD_SET(fd, fdSet);

        this.timeVal = new TimeVal();
        this.timeVal.tv_sec = 10;

        if (dataListener != null) {
            thread.start();
        }
    }

    public void write(byte[] data) throws IOException {
        int n = JTermios.write(fd, data, data.length);
        if (n < 0) {
            throw new IOException("write failed");
        }
    }

    class Reader implements Runnable {
        public void run() {
            try {
                byte[] buffer = new byte[1000];
                int s = select(fd + 1, fdSet, null, null, timeVal);
                if (s < 0) {
                    throw new IOException("select failed");
                }
                for (; ; ) {
                    int l = JTermios.read(fd, buffer, buffer.length);
                    if (l < 0) {
                        throw new IOException("read failed");
                    }
                    dataListener.receive(buffer, l);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void close() {
        if (dataListener != null) {
            thread.interrupt();
        }
        JTermios.close(fd);
    }
}
