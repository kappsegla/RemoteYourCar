package se.iths.remoteyourcar.routing;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.*;
import se.iths.remoteyourcar.repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class VehicleService {

    Map<Long, VehicleState> vehicleStateMap = new HashMap<>();

    private VehicleRepository repository;

    Random random = new Random();

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

    public Mono<Response> lockDoors(@Parameter(in = ParameterIn.PATH) Long id) {
        VehicleState vehicleState = repository.findById(id);
        vehicleState.getDoorState().setAllLocked();
        vehicleState.setLocked(true);

        Response response = new Response();
        response.setReason("");
        response.setResult(true);
        return Mono.just(response);
    }

    public Mono<Response> unLockDoors(@Parameter(in = ParameterIn.PATH) Long id) {
        VehicleState vehicleState = repository.findById(id);
        vehicleState.getDoorState().setAllUnLocked();
        vehicleState.setLocked(false);

        Response response = new Response();
        response.setReason("");
        response.setResult(true);
        return Mono.just(response);
    }

    /**
     * Returns a list of all vehicles that the current authenticated user has access to.
     * Vin generated with https://randomvin.com/
     */
    public Flux<Vehicle> getVehicles() {
        Vehicle v1 = new Vehicle();
        v1.setCarId(1);
        v1.setVin("2C4RC1BG9DR681530");
        v1.setName("ITHS");
        v1.setColor("Black");
        Vehicle v2 = new Vehicle();
        v2.setCarId(2);
        v2.setVin("5TFRY5F13CX149897");
        v2.setName("ITHS");
        v2.setColor("Red");
        return Flux.just(v1, v2);
    }

    public Mono<ClimateState> getClimateState(@Parameter(in = ParameterIn.PATH) Long id) {
        ClimateState climateState = repository.findClimateStateById(id);
        if (climateState.getTimestamp() - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) > 60 * 60) {
            //More than one hour since last check. Temperature has changed alot.
            if (Math.random() < 0.5) {
                climateState.setInside_temp(climateState.getInside_temp() + random.nextInt(5));
                climateState.setOutside_temp(climateState.getOutside_temp() + random.nextInt(5));
            } else {
                climateState.setInside_temp(climateState.getInside_temp() - random.nextInt(5));
                climateState.setOutside_temp(climateState.getOutside_temp() - random.nextInt(5));
            }
        }
        if (Math.random() < 0.1) {
            if (Math.random() < 0.5)
                climateState.setInside_temp(climateState.getInside_temp() + 0.5f);
            else
                climateState.setInside_temp(climateState.getInside_temp() - 0.5f);
        }
        if (Math.random() < 0.1) {
            if (Math.random() < 0.5)
                climateState.setOutside_temp(climateState.getOutside_temp() + 0.5f);
            else
                climateState.setOutside_temp(climateState.getOutside_temp() - 0.5f);
        }
        climateState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        return Mono.just(climateState);
    }

    public Mono<Response> setTemperature(@Parameter(in = ParameterIn.PATH) Long id, float driver_temp, float passenger_temp) {
        ClimateState climateState = repository.findClimateStateById(id);
        climateState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        climateState.setDriver_temp_setting(driver_temp);
        climateState.setPassenger_temp_setting(passenger_temp);

        Response response = new Response();
        response.setReason("");
        response.setResult(true);
        return Mono.just(response);
    }
}
