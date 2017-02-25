package net.catchpole.B9.tools;

import net.catchpole.B9.devices.serial.PiSerialPort;
import net.catchpole.B9.devices.serial.SocketListenerSerialPort;
import net.catchpole.B9.lang.Arguments;

// want to use a serial device remotely?
// you can even do remote development by using the SocketSerialPort
// and then switch to the PiSerialPort when running that code on a Pi
// dont forget you can tunnel sockets to your pi - to tunnel port 4000 use..
//   ssh -L 4000:localhost:4000 pi@mypi.address
// and then use
//   new SocketSerialPort("localhost", 4000)
public class PiSerialProxy {
    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);
        int tcpPort = arguments.getArgumentProperty("-port", 8000);
        int baud = arguments.getArgumentProperty("-baud", 9600);
        SocketListenerSerialPort socketListenerSerialPort = new SocketListenerSerialPort(
                new PiSerialPort(), tcpPort, baud);
        socketListenerSerialPort.accept();
    }
}
