package se.iths.remoteyourcar.routing;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.*;
import se.iths.remoteyourcar.repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Predicate;

@Component
public class VehicleService {

    private VehicleRepository repository;

    Random random = new Random();

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public Mono<DriveState> getDriveState(@Parameter(in = ParameterIn.PATH) Long id) {
        return getCredentials()
                .filter(Predicate.isEqual(id))
                .next()
                .map(i -> {
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
                    return driveState;
                });
    }

    public Mono<VehicleState> getVehicleState(@Parameter(in = ParameterIn.PATH) Long id) {
        return getCredentials()
                .filter(Predicate.isEqual(id))
                .next()
                .flatMap(i -> repository.findVehicleStateById(id));
    }

    public Mono<Response> lockDoors(@Parameter(in = ParameterIn.PATH) Long id) {
        return getCredentials()
                .filter(Predicate.isEqual(id))
                .next()
                .flatMap(i -> repository.findVehicleStateById(i))
                .map(vehicleState -> {
                    vehicleState.getDoorState().setAllLocked();
                    vehicleState.setLocked(true);
                    vehicleState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

                    repository.saveVehicleState(vehicleState);

                    Response response = new Response();
                    response.setReason("");
                    response.setResult(true);
                    return response;
                });
    }

    public Mono<Response> unLockDoors(@Parameter(in = ParameterIn.PATH) Long id) {
        return getCredentials()
                .filter(Predicate.isEqual(id))
                .next()
                .flatMap(i -> repository.findVehicleStateById(i))
                .map(vehicleState -> {
                    vehicleState.getDoorState().setAllUnLocked();
                    vehicleState.setLocked(false);
                    vehicleState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

                    repository.saveVehicleState(vehicleState);

                    Response response = new Response();
                    response.setReason("");
                    response.setResult(true);
                    return response;
                });
    }

    /**
     * Returns a list of all vehicles that the current authenticated user has access to.
     * Vin generated with https://randomvin.com/
     */
    public Flux<Vehicle> getVehicles() {
        return getCredentials().flatMap(i -> repository.findVehicleById(i));
    }

    public Mono<ClimateState> getClimateState(@Parameter(in = ParameterIn.PATH) Long id) {
        return getCredentials()
                .filter(Predicate.isEqual(id))
                .next()
                .map(i -> {
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
                    return climateState;
                });
    }

    public Mono<Response> setTemperature(@Parameter(in = ParameterIn.PATH) Long id, float driver_temp, float passenger_temp) {
        return getCredentials()
                .filter(Predicate.isEqual(id))
                .next()
                .flatMap(i -> {
                    ClimateState climateState = repository.findClimateStateById(id);
                    climateState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

                    if (driver_temp < climateState.getMin_avail_temp() || driver_temp > climateState.getMax_avail_temp()
                            || passenger_temp < climateState.getMin_avail_temp() || passenger_temp > climateState.getMax_avail_temp()
                            || driver_temp == -1 || passenger_temp == -1)
                         return Mono.empty();

                    climateState.setDriver_temp_setting(driver_temp);
                    climateState.setPassenger_temp_setting(passenger_temp);

                    Response response = new Response();
                    response.setReason("");
                    response.setResult(true);
                    return Mono.just(response);
                });
    }

    public Mono<String> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(Object::toString);
    }

    public Flux<String> getAuthorities() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .flatMapMany(Flux::fromIterable)
                .map(GrantedAuthority::getAuthority);
    }

    public Flux<Long> getCredentials() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getCredentials)
                .map(c -> (List<Long>) c)
                .flatMapMany(Flux::fromIterable);
    }
}
