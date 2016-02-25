package net.catchpole.B9.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTransceiver implements PacketTarget {
    private int port;
    private InetAddress targetHost;
    private byte[] receiveBuffer = new byte[2000];
    private DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
    private DatagramSocket listenSocket;
    private PacketTarget packetTarget;

    public UDPTransceiver(String targetHost, int port, PacketTarget packetTarget) throws IOException {
        this.targetHost = InetAddress.getByName(targetHost);
        this.port = port;
        this.listenSocket = new DatagramSocket(port);
        this.packetTarget = packetTarget;
    }

    public void send(byte[] data) {
        DatagramPacket outPacket = new DatagramPacket(data, data.length, targetHost, port);
        try {
            listenSocket.send(outPacket);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void listen() {
        for (;;) {
            try {
                listenSocket.receive(receivePacket);
                byte[] data = new byte[ receivePacket.getLength() ];
                System.arraycopy(receivePacket.getData(), 0, data, 0, data.length);
                packetTarget.send(data);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
