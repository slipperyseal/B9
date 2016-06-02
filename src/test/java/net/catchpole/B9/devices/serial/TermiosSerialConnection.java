package net.catchpole.B9.devices.serial;

import jtermios.JTermios;
import jtermios.Termios;
import jtermios.TimeVal;

import java.io.IOException;

public class TermiosSerialConnection implements SerialConnection {
    private final int fd;
    private final TimeVal timeVal;
    private final JTermios.FDSet fdSet = JTermios.newFDSet();
    private Thread thread = new Thread(new Reader());
    private DataListener dataListener;

    public TermiosSerialConnection(String port, int baud, DataListener dataListener) throws IOException {
        this.dataListener = dataListener;
        this.fd = JTermios.open(port == null ? "/dev/tty.wchusbserial1410" : port, JTermios.O_RDWR | JTermios.O_NOCTTY | JTermios.O_NONBLOCK);
        if (this.fd == -1) {
            throw new IOException("unable to open " + port + " error " + fd);
        }
        JTermios.fcntl(fd, JTermios.F_SETFL, 0);

        Termios termios = new Termios();
        JTermios.tcgetattr(fd, termios);
        termios.c_lflag &= ~(JTermios.ICANON | JTermios.ECHO | JTermios.ECHOE | JTermios.ISIG);
        termios.c_cflag |= (JTermios.CLOCAL | JTermios.CREAD);
        termios.c_cflag &= ~JTermios.PARENB;
        termios.c_cflag |= JTermios.CSTOPB;
        termios.c_cflag &= ~JTermios.CSIZE;
        termios.c_cflag |= JTermios.CS8;
        termios.c_oflag &= ~JTermios.OPOST;
        termios.c_iflag &= ~JTermios.INPCK;
        termios.c_iflag &= ~(JTermios.IXON | JTermios.IXOFF | JTermios.IXANY);
        termios.c_cc[JTermios.VMIN] = 0;
        termios.c_cc[JTermios.VTIME] = 10;

        JTermios.cfsetispeed(termios, baud);
        JTermios.cfsetospeed(termios, baud);

        JTermios.tcsetattr(fd, JTermios.TCSANOW, termios);

        JTermios.tcflush(fd, JTermios.TCIOFLUSH);

        JTermios.FD_ZERO(fdSet);
        JTermios.FD_SET(fd, fdSet);

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

    // sometimes i get rubbish data from this read. i think it's to do with a Thread blocking on the read
    // while another thread is performing a write.
    // this Thread is to support the same model as the PI4J comm port which pushes writes
    class Reader implements Runnable {
        public void run() {
            try {
                byte[] buffer = new byte[1000];
                int s = JTermios.select(fd + 1, fdSet, null, null, timeVal);
                if (s < 0) {
                    throw new IOException("select failed");
                }
                for (; ; ) {
                    int l = JTermios.read(fd, buffer, buffer.length);
                    if (l < 0) {
                        throw new IOException("read failed");
                    }
                    if (l > 0) {
                        dataListener.receive(buffer, l);
                    }
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
