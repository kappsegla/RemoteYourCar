package se.iths.remoteyourcar.routing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.DoorState;
import se.iths.remoteyourcar.entities.DriveState;
import se.iths.remoteyourcar.entities.VehicleState;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration(proxyBeanMethods = false)
public class VehicleRouter {

    //https://tesla-api.timdorr.com/
    //https://www.teslaapi.io/
    private final VehicleService vehicleService;

    public VehicleRouter(VehicleService handler) {
        this.vehicleService = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route()
                .GET("/api/1/vehicles/{id}/data_request/drive_state",
                        this::getDriveState, ops -> ops.beanClass(VehicleService.class).beanMethod("getDriveState"))

                .GET("/api/1/vehicles/{id}/data_request/vehicle_state",
                        this::getVehicleState, ops -> ops.beanClass(VehicleService.class).beanMethod("getVehicleState"))

                .POST("/api/1/vehicles/{id}/command/door_lock",
                        this::lockDoors, ops -> ops.beanClass(VehicleService.class).beanMethod("lockDoors"))

                .POST("/api/1/vehicles/{id}/command/door_unlock",
                        this::unLockDoors, ops -> ops.beanClass(VehicleService.class).beanMethod("unLockDoors")).build();
    }

    private Mono<ServerResponse> getVehicleState(ServerRequest request) {
        return ServerResponse.ok()
                .body(vehicleService.getVehicleState(Long.parseLong(request.pathVariable("id"))), VehicleState.class);
    }

    private Mono<ServerResponse> getDriveState(ServerRequest request) {
        return ServerResponse.ok()
                .body(vehicleService.getDriveState(Long.parseLong(request.pathVariable("id"))), DriveState.class);
    }

    private Mono<ServerResponse> lockDoors(ServerRequest request) {
        return ServerResponse.ok()
                .body(vehicleService.lockDoors(Long.parseLong(request.pathVariable("id"))), DoorState.class);
    }

    private Mono<ServerResponse> unLockDoors(ServerRequest request) {
        return ServerResponse.ok()
                .body(vehicleService.unLockDoors(Long.parseLong(request.pathVariable("id"))), DoorState.class);
    }
}
