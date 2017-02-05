package net.catchpole.B9.devices.rockblock.message;

public class ShortBurstDataInitiateSession {
    private int originatingStatus;
    private int originatingSequence;
    private int terminatedStatus;
    private int terminatedSequence;
    private int terminatedLength;
    private int gatewayQueuedCount;

    public ShortBurstDataInitiateSession(String[] data) {
        this.originatingStatus = Integer.parseInt(data[0]);
        this.originatingSequence = Integer.parseInt(data[1]);
        this.terminatedStatus = Integer.parseInt(data[2]);
        this.terminatedSequence = Integer.parseInt(data[3]);
        this.terminatedLength = Integer.parseInt(data[4]);
        this.gatewayQueuedCount = Integer.parseInt(data[5]);
    }

    public int getOriginatingStatus() {
        return originatingStatus;
    }

    public int getOriginatingSequence() {
        return originatingSequence;
    }

    public int getTerminatedStatus() {
        return terminatedStatus;
    }

    public int getTerminatedSequence() {
        return terminatedSequence;
    }

    public int getTerminatedLength() {
        return terminatedLength;
    }

    public int getGatewayQueuedCount() {
        return gatewayQueuedCount;
    }

    @Override
    public String toString() {
        return "ShortBurstDataInitiateSession{" +
                "originatingStatus=" + originatingStatus +
                ". originatingSequence=" + originatingSequence +
                ", terminatedStatus=" + terminatedStatus +
                ", terminatedSequence=" + terminatedSequence +
                ", terminatedLength=" + terminatedLength +
                ", gatewayQueuedCount=" + gatewayQueuedCount +
                '}';
    }
}
