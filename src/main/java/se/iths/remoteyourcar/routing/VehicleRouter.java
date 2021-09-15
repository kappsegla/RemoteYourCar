package se.iths.remoteyourcar.routing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class VehicleRouter {

    //https://tesla-api.timdorr.com/
    //https://www.teslaapi.io/

    @Bean
    public RouterFunction<ServerResponse> route(VehicleHandler vehicleHandler) {

        return RouterFunctions
                .route(GET("/api/1/vehicles/{id}/data_request/drive_state")
                        .and(accept(MediaType.APPLICATION_JSON)), vehicleHandler::driveState)
                .andRoute(GET("/api/1/vehicles/{id}/data_request/vehicle_state")
                        .and(accept(MediaType.APPLICATION_JSON)), vehicleHandler::vehicleState)
                .andRoute(POST("/api/1/vehicles/{id}/command/door_unlock")
                .and(accept(MediaType.APPLICATION_JSON)), vehicleHandler::unlockDoors)
                .andRoute(POST("/api/1/vehicles/{id}/command/door_lock")
                .and(accept(MediaType.APPLICATION_JSON)), vehicleHandler::lockDoors);
    }
}
