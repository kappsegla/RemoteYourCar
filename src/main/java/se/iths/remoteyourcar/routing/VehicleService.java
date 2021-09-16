package se.iths.remoteyourcar.routing;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.DoorResponse;
import se.iths.remoteyourcar.entities.DoorState;
import se.iths.remoteyourcar.entities.DriveState;
import se.iths.remoteyourcar.entities.VehicleState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Component
public class VehicleService {

    Map<Long, VehicleState> vehicleStateMap = new HashMap<>();

    private VehicleState getNewOrExistingVehicleState(Long carId) {
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

    public Mono<DriveState> getDriveState(@Parameter(in = ParameterIn.PATH) Long id) {
        var driveState = new DriveState();
        driveState.setCarId(id);
        driveState.setGpsAsOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        driveState.setHeading(70);
        driveState.setLatitude(57.683688221408474);
        driveState.setLongitude(12.00813226724387);
        driveState.setSpeed(0);
        driveState.setPower(0);
        driveState.setShiftState("P");
        driveState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        return Mono.just(driveState);
    }

    public Mono<VehicleState> getVehicleState(@Parameter(in = ParameterIn.PATH) Long id) {
        VehicleState vehicleState = getNewOrExistingVehicleState(id);
        vehicleState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        return Mono.just(vehicleState);
    }

    public Mono<DoorResponse> lockDoors(@Parameter(in = ParameterIn.PATH) Long id) {
        VehicleState vehicleState = getNewOrExistingVehicleState(id);
        vehicleState.getDoorState().setAllLocked();

        DoorResponse response = new DoorResponse();
        response.setReason("");
        response.setResult(true);
        return Mono.just(response);
    }

    public Mono<DoorResponse> unLockDoors(@Parameter(in = ParameterIn.PATH) Long id) {
        VehicleState vehicleState = getNewOrExistingVehicleState(id);
        vehicleState.getDoorState().setAllUnLocked();

        DoorResponse response = new DoorResponse();
        response.setReason("");
        response.setResult(true);
        return Mono.just(response);
    }
}
