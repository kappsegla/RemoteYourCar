package se.iths.remoteyourcar.entities;

import javax.persistence.*;

/**
 * df - driver front
 * dr - driver rear
 * pf - passenger front
 * pr - passenger rear
 * ft - front trunk
 * rt - rear trunk
 */
@Entity
@Table(name="vehilcestates")
public class VehicleState {
    @Id
    @Column(name = "car_id", nullable = false)
    private long carId;
    private long timestamp;

    private boolean locked;

    @OneToOne(cascade = CascadeType.ALL)
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

