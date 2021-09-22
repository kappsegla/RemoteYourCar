package se.iths.remoteyourcar.routing;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.DoorResponse;
import se.iths.remoteyourcar.entities.DriveState;
import se.iths.remoteyourcar.entities.VehicleState;
import se.iths.remoteyourcar.repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Component
public class VehicleService {

    Map<Long, VehicleState> vehicleStateMap = new HashMap<>();

    private VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public Mono<DriveState> getDriveState(@Parameter(in = ParameterIn.PATH) Long id) {
        if (id < 1 || id > 100)
            return Mono.empty();
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
        VehicleState vehicleState = repository.findById(id);
        vehicleState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        return Mono.just(vehicleState);
    }

    public Mono<DoorResponse> lockDoors(@Parameter(in = ParameterIn.PATH) Long id) {
        VehicleState vehicleState = repository.findById(id);
        vehicleState.getDoorState().setAllLocked();
        vehicleState.setLocked(true);

        DoorResponse response = new DoorResponse();
        response.setReason("");
        response.setResult(true);
        return Mono.just(response);
    }

    public Mono<DoorResponse> unLockDoors(@Parameter(in = ParameterIn.PATH) Long id) {
        VehicleState vehicleState = repository.findById(id);
        vehicleState.getDoorState().setAllUnLocked();
        //vehicleState.setLocked(false);  This is a bug, added for testing purposes.

        DoorResponse response = new DoorResponse();
        response.setReason("");
        response.setResult(true);
        return Mono.just(response);
    }
}
