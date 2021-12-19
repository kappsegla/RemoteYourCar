package se.iths.remoteyourcar.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.retry.Repeat;
import reactor.util.retry.Retry;
import se.iths.remoteyourcar.entities.ClimateState;
import se.iths.remoteyourcar.entities.DoorState;
import se.iths.remoteyourcar.entities.Vehicle;
import se.iths.remoteyourcar.entities.VehicleState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Repository
public class VehicleRepository {

    Map<Long, ClimateState> climateStateMap = new HashMap<>();

    VehicleCrudRepository vehicleCrudRepository;
    VehicleStateRepository vehicleStateRepository;

    public VehicleRepository(VehicleCrudRepository vehicleCrudRepository, VehicleStateRepository vehicleStateRepository) {
        this.vehicleCrudRepository = vehicleCrudRepository;
        this.vehicleStateRepository = vehicleStateRepository;
    }

    public Mono<VehicleState> findVehicleStateById(Long carId) {
        Optional<VehicleState> vehicleState = vehicleStateRepository.findById(carId);
        return vehicleState.map(Mono::just)
                .orElseGet(
                        () -> createNewVehicleState(carId)
                                .doOnNext(vehicleStateRepository::save));
    }

    private Mono<VehicleState> createNewVehicleState(Long key) {
        VehicleState vehicleState = new VehicleState();
        vehicleState.setCarId(key);
        vehicleState.setLocked(false);
        vehicleState.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        DoorState doorState = new DoorState();
        doorState.setCarID(key);
        doorState.setAllLocked();
        doorState.setDf_locked(false);  //Only driverside door unlocked
        vehicleState.setDoorState(doorState);
        return Mono.just(vehicleState);
    }

    public ClimateState findClimateStateById(Long carId) {
        return climateStateMap.computeIfAbsent(carId, key -> {
            ClimateState climateState = new ClimateState();
            climateState.setCarId(key);
            return climateState;
        });
    }

    public Mono<Vehicle> findVehicleById(Long carId) {
        Optional<Vehicle> vehicle = vehicleCrudRepository.findById(carId);
        return vehicle.map(Mono::just).orElseGet(
                () -> createNewVehicle(carId).doOnNext(v -> vehicleCrudRepository.save(v)));
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

        return vin.flatMap(s -> {
            v1.setVin(s);
            return Mono.just(v1);
        });
    }

    private Mono<String> getRandomVin() {
        WebClient client = WebClient.create();

        return client.get().uri("https://randomvin.com/getvin.php?type=fake")
                .retrieve()
                .bodyToMono(String.class)
                .map(s->s.replaceAll("[^a-zA-Z0-9]", ""))
                .filter(response -> !response.isEmpty())
                .repeatWhenEmpty(Repeat.onlyIf(r -> true)
                        .fixedBackoff(Duration.ofSeconds(1))
                        .timeout(Duration.ofSeconds(5)))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)));
    }

    public VehicleState saveVehicleState(VehicleState vehicleState) {
        return vehicleStateRepository.save(vehicleState);
    }
}
