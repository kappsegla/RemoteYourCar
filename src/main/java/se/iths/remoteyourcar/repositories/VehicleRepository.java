package se.iths.remoteyourcar.repositories;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.DoorState;
import se.iths.remoteyourcar.entities.VehicleState;
import se.iths.remoteyourcar.routing.VehicleService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class VehicleRepository {

    Map<Long, VehicleState> vehicleStateMap = new ConcurrentHashMap<>();

    public VehicleState findById(Long carId) {
        return vehicleStateMap.computeIfAbsent(carId, key -> {
            VehicleState vehicleState = new VehicleState();
            vehicleState.setCarId(carId);
            vehicleState.setLocked(false);
            vehicleState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            DoorState doorState = new DoorState();
            doorState.setAllLocked();
            doorState.setDf_locked(false);  //Only driverside door unlocked
            vehicleState.setDoorState(doorState);
            return vehicleState;
        });
    }
}
