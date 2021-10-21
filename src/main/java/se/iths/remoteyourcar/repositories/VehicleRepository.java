package se.iths.remoteyourcar.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import se.iths.remoteyourcar.entities.ClimateState;
import se.iths.remoteyourcar.entities.DoorState;
import se.iths.remoteyourcar.entities.Vehicle;
import se.iths.remoteyourcar.entities.VehicleState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Repository
public class VehicleRepository {

    Map<Long, VehicleState> vehicleStateMap = new HashMap<>();
    Map<Long, ClimateState> climateStateMap = new HashMap<>();
    Map<Long, Vehicle> vehicleMap = new HashMap<>();

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

    public Mono<Vehicle> findVehicleById(Long carId) {
        if (vehicleMap.containsKey(carId))
            return Mono.just(vehicleMap.get(carId));
        else
            return createNewVehicle(carId).doOnNext(vehicle -> vehicleMap.put(vehicle.getCarId(),vehicle));
    }

    private Mono<Vehicle> createNewVehicle(Long carId) {
        List<String> colors = List.of("BLACK", "BLUE", "ORANGE",
                "WHITE", "GRAY", "GREEN", "RED", "PINK", "DARK_GRAY", "LIGHT_GRAY",
                "YELLOW");
        List<String> names = List.of("ITHS", "Model P", "WC90",
                "Terminator", "Festvagn", "Turtle Taxi", "Humla",
                "Desdamona", "Eleanor", "Brum",
                "Clown Mobile", "Guppa", "Selena", "Zinger");

        Mono<String> vin = getRandomVin();

        Vehicle v1 = new Vehicle();
        v1.setCarId(carId);
        int randomNumber = new Random().nextInt(14);
        v1.setName(names.get(randomNumber));
        randomNumber = new Random().nextInt(11);
        v1.setColor(colors.get(randomNumber));

        return vin.flatMap(s -> { v1.setVin(s.substring(1).trim()); return Mono.just(v1);});
    }

    private Mono<String> getRandomVin() {
        WebClient client = WebClient.create();

        return client.get().uri("https://randomvin.com/getvin.php?type=fake")
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
    }
}
