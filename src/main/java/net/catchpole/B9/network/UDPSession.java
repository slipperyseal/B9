package net.catchpole.B9.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A UDPSession is a UDP listener which remembers the address and port number of the last reply it receives.
 * Packets sent through the UDPSession will be directed to the remembered address and port (or dropped if this doesn't exist yet)
 * This allows reply traffic to respond via NAT firewalls
 */
public class UDPSession implements PacketTarget {
    private byte[] receiveBuffer = new byte[2000];
    private DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
    private DatagramSocket socket;
    private PacketTarget packetTarget;

    private volatile InetAddress lastAddress;
    private volatile int lastPort;

    public UDPSession(int port) throws IOException {
        this.socket = new DatagramSocket(port);
    }

    public void setPacketTarget(PacketTarget packetTarget) {
        this.packetTarget = packetTarget;
    }

    public void send(byte[] data) {
        if (lastAddress != null) {
            DatagramPacket outPacket = new DatagramPacket(data, data.length, lastAddress, lastPort);
            try {
                socket.send(outPacket);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            System.out.print('>');
        }
    }

    public void listen() {
        for (;;) {
            try {
                this.socket.receive(receivePacket);
                this.lastAddress = receivePacket.getAddress();
                this.lastPort = receivePacket.getPort();

                System.out.print('<');
                byte[] data = new byte[ receivePacket.getLength() ];
                System.arraycopy(receivePacket.getData(), 0, data, 0, data.length);
                packetTarget.send(data);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
