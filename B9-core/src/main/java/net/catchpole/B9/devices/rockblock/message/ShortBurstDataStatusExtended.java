package net.catchpole.B9.devices.rockblock.message;

public class ShortBurstDataStatusExtended {
    private boolean originatingBuffer;
    private int originatingSequence;
    private boolean terminatedBuffer;
    private int terminatedSequence;
    private boolean ring;
    private int gatewayQueuedCount;

    public ShortBurstDataStatusExtended(String[] data) {
        this.originatingBuffer = data[0].equals("1");
        this.originatingSequence = Integer.parseInt(data[1]);
        this.terminatedBuffer = data[2].equals("1");
        this.terminatedSequence = Integer.parseInt(data[3]);
        this.ring = data[4].equals("1");
        this.gatewayQueuedCount = Integer.parseInt(data[5]);
    }

    public boolean isOriginatingBuffer() {
        return originatingBuffer;
    }

    public int getOriginatingSequence() {
        return originatingSequence;
    }

    public boolean isTerminatedBuffer() {
        return terminatedBuffer;
    }

    public int getTerminatedSequence() {
        return terminatedSequence;
    }

    public boolean isRing() {
        return ring;
    }

    public int getGatewayQueuedCount() {
        return gatewayQueuedCount;
    }

    @Override
    public String toString() {
        return "ShortBurstDataStatusExtended{" +
                "originatingBuffer=" + originatingBuffer +
                ", originatingSequence=" + originatingSequence +
                ", terminatedBuffer=" + terminatedBuffer +
                ", terminatedSequence=" + terminatedSequence +
                ", ring=" + ring +
                ", gatewayQueuedCount=" + gatewayQueuedCount +
                '}';
    }
}
