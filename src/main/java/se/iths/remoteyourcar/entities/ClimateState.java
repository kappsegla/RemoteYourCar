package se.iths.remoteyourcar.entities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ClimateState {
    private long carId;
    private float inside_temp = 12.5f;
    private float outside_temp = 10.0f;
    private float driver_temp_setting = 18.3f;
    private float passenger_temp_setting = 18.3f;
    private float min_avail_temp = 15.0f;
    private float max_avail_temp = 28.0f;

    private boolean is_front_defroster_on = false;
    private boolean is_rear_defroster_on = false;
    private boolean seat_heater_left = false;
    private boolean seat_heater_right = false;
    private boolean seat_heater_rear_left = false;
    private boolean seat_heater_rear_right = false;
    private boolean seat_heater_rear_center = false;
    private boolean battery_heater = false;
    private boolean steering_wheel_heater = false;
    private boolean wiper_blade_heater = false;
    private boolean side_mirror_heaters = false;
    private long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

    public float getMin_avail_temp() {
        return min_avail_temp;
    }

    public void setMin_avail_temp(float min_avail_temp) {
        this.min_avail_temp = min_avail_temp;
    }

    public float getMax_avail_temp() {
        return max_avail_temp;
    }

    public void setMax_avail_temp(float max_avail_temp) {
        this.max_avail_temp = max_avail_temp;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public float getInside_temp() {
        return inside_temp;
    }

    public void setInside_temp(float inside_temp) {
        this.inside_temp = inside_temp;
    }

    public float getOutside_temp() {
        return outside_temp;
    }

    public void setOutside_temp(float outside_temp) {
        this.outside_temp = outside_temp;
    }

    public float getDriver_temp_setting() {
        return driver_temp_setting;
    }

    public void setDriver_temp_setting(float driver_temp_setting) {
        this.driver_temp_setting = driver_temp_setting;
    }

    public float getPassenger_temp_setting() {
        return passenger_temp_setting;
    }

    public void setPassenger_temp_setting(float passenger_temp_setting) {
        this.passenger_temp_setting = passenger_temp_setting;
    }

    public boolean isIs_front_defroster_on() {
        return is_front_defroster_on;
    }

    public void setIs_front_defroster_on(boolean is_front_defroster_on) {
        this.is_front_defroster_on = is_front_defroster_on;
    }

    public boolean isIs_rear_defroster_on() {
        return is_rear_defroster_on;
    }

    public void setIs_rear_defroster_on(boolean is_rear_defroster_on) {
        this.is_rear_defroster_on = is_rear_defroster_on;
    }

    public boolean isSeat_heater_left() {
        return seat_heater_left;
    }

    public void setSeat_heater_left(boolean seat_heater_left) {
        this.seat_heater_left = seat_heater_left;
    }

    public boolean isSeat_heater_right() {
        return seat_heater_right;
    }

    public void setSeat_heater_right(boolean seat_heater_right) {
        this.seat_heater_right = seat_heater_right;
    }

    public boolean isSeat_heater_rear_left() {
        return seat_heater_rear_left;
    }

    public void setSeat_heater_rear_left(boolean seat_heater_rear_left) {
        this.seat_heater_rear_left = seat_heater_rear_left;
    }

    public boolean isSeat_heater_rear_right() {
        return seat_heater_rear_right;
    }

    public void setSeat_heater_rear_right(boolean seat_heater_rear_right) {
        this.seat_heater_rear_right = seat_heater_rear_right;
    }

    public boolean isSeat_heater_rear_center() {
        return seat_heater_rear_center;
    }

    public void setSeat_heater_rear_center(boolean seat_heater_rear_center) {
        this.seat_heater_rear_center = seat_heater_rear_center;
    }

    public boolean isBattery_heater() {
        return battery_heater;
    }

    public void setBattery_heater(boolean battery_heater) {
        this.battery_heater = battery_heater;
    }

    public boolean isSteering_wheel_heater() {
        return steering_wheel_heater;
    }

    public void setSteering_wheel_heater(boolean steering_wheel_heater) {
        this.steering_wheel_heater = steering_wheel_heater;
    }

    public boolean isWiper_blade_heater() {
        return wiper_blade_heater;
    }

    public void setWiper_blade_heater(boolean wiper_blade_heater) {
        this.wiper_blade_heater = wiper_blade_heater;
    }

    public boolean isSide_mirror_heaters() {
        return side_mirror_heaters;
    }

    public void setSide_mirror_heaters(boolean side_mirror_heaters) {
        this.side_mirror_heaters = side_mirror_heaters;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
