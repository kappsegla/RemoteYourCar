package se.iths.remoteyourcar.repositories;

import org.springframework.stereotype.Repository;
import se.iths.remoteyourcar.entities.ClimateState;
import se.iths.remoteyourcar.entities.DoorState;
import se.iths.remoteyourcar.entities.VehicleState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Repository
public class VehicleRepository {

    Map<Long, VehicleState> vehicleStateMap = new HashMap<>();
    Map<Long, ClimateState> climateStateMap = new HashMap<>();

    public VehicleState findById(Long carId) {
        return vehicleStateMap.computeIfAbsent(carId, key -> {
            VehicleState vehicleState = new VehicleState();
            vehicleState.setCarId(key);
            vehicleState.setLocked(false);
            vehicleState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            DoorState doorState = new DoorState();
            doorState.setAllLocked();
            doorState.setDf_locked(false);  //Only driverside door unlocked
            vehicleState.setDoorState(doorState);
            return vehicleState;
        });
    }

    public ClimateState findClimateStateById(Long carId) {
        return climateStateMap.computeIfAbsent(carId, key -> {
            ClimateState climateState = new ClimateState();
            climateState.setCarId(key);
            return climateState;
        });
    }
}
