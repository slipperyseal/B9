package net.catchpole.B9.devices.gps.message;

public class RecommendedMinimum {
    private String time;
    private char status;
    private Location location;
    private double speedOverGroundKnots;
    private double trackAngle;
    private double magneticVariation;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getSpeedOverGroundKnots() {
        return speedOverGroundKnots;
    }

    public void setSpeedOverGroundKnots(double speedOverGroundKnots) {
        this.speedOverGroundKnots = speedOverGroundKnots;
    }

    public double getTrackAngle() {
        return trackAngle;
    }

    public void setTrackAngle(double trackAngle) {
        this.trackAngle = trackAngle;
    }

    public double getMagneticVariation() {
        return magneticVariation;
    }

    public void setMagneticVariation(double magneticVariation) {
        this.magneticVariation = magneticVariation;
    }

    @Override
    public String toString() {
        return "RecommendedMinimum{" +
                "time='" + time + '\'' +
                ", status=" + status +
                ", location=" + location +
                ", speedOverGroundKnots=" + speedOverGroundKnots +
                ", trackAngle=" + trackAngle +
                ", magneticVariation=" + magneticVariation +
                '}';
    }
}
