package se.iths.remoteyourcar.entities;

/**
 * df - driver front
 * dr - driver rear
 * pf - passenger front
 * pr - passenger rear
 * ft - front trunk
 * rt - rear trunk
 */
public class VehicleState {

    private long carId;
    private long timestamp;

    private boolean locked;
    private DoorState doorState;

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public DoorState getDoorState() {
        return doorState;
    }

    public void setDoorState(DoorState doorState) {
        this.doorState = doorState;
    }
}

