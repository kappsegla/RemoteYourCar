package se.iths.remoteyourcar.routing;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.DriveState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class VehicleHandler {
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
}
