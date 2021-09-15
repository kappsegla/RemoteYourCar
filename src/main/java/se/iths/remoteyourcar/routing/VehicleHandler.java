package se.iths.remoteyourcar.routing;

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
public class VehicleHandler {
    Map<Long, VehicleState> vehicleStateMap = new HashMap<>();

    private VehicleState getNewOrExistingVehicleState(Long carId){
        return vehicleStateMap.computeIfAbsent(carId,key -> {
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

    public Mono<ServerResponse> driveState(ServerRequest request) {

        var carId = Long.parseLong(request.pathVariable("id"));
        var driveState = new DriveState();
        driveState.setCarId(carId);
        driveState.setGpsAsOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        driveState.setHeading(70);
        driveState.setLatitude(57.683688221408474);
        driveState.setLongitude(12.00813226724387);
        driveState.setSpeed(0);
        driveState.setPower(0);
        driveState.setShiftState("P");
        driveState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(driveState));
    }

    public Mono<ServerResponse> vehicleState(ServerRequest request){
        var carId = Long.parseLong(request.pathVariable("id"));
        VehicleState vehicleState = getNewOrExistingVehicleState(carId);
        vehicleState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(vehicleState));
    }
    public Mono<ServerResponse> lockDoors(ServerRequest request){
        var carId = Long.parseLong(request.pathVariable("id"));
        VehicleState vehicleState = getNewOrExistingVehicleState(carId);
        vehicleState.getDoorState().setAllLocked();

        DoorResponse response = new DoorResponse();
        response.setReason("");
        response.setResult(true);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(response));
    }

    public Mono<ServerResponse> unlockDoors(ServerRequest request){
        var carId = Long.parseLong(request.pathVariable("id"));
        VehicleState vehicleState = getNewOrExistingVehicleState(carId);
        vehicleState.getDoorState().setAllUnLocked();

        DoorResponse response = new DoorResponse();
        response.setReason("");
        response.setResult(true);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(response));
    }
}
