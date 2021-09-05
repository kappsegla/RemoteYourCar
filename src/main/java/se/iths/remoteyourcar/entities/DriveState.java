package se.iths.remoteyourcar.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DriveState {

    private long carId;

    private long gpsAsOf;

    private int heading;

    private double latitude;

    private double longitude;

    private int power;

    private String shiftState;

    private int speed;

    private long timestamp;

    public long getGpsAsOf() {
        return gpsAsOf;
    }

    public void setGpsAsOf(long gpsAsOf) {
        this.gpsAsOf = gpsAsOf;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @JsonProperty("shift_state")
    public String getShiftState() {
        return shiftState;
    }

    public void setShiftState(String shiftState) {
        this.shiftState = shiftState;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }
}
