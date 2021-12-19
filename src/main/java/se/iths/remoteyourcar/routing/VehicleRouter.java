package se.iths.remoteyourcar.routing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.Response;
import se.iths.remoteyourcar.entities.Vehicle;

import java.util.function.Predicate;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.core.fn.builders.securityrequirement.Builder.securityRequirementBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

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
                        this::getDriveState, ops -> ops
                                .beanClass(VehicleService.class)
                                .beanMethod("getDriveState")
                                .operationId("drive_state")
                                .response(responseBuilder().responseCode("200").description("This is normal response for ids retrieved from vehicles endpoint"))
                                .response(responseBuilder().responseCode("404")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Vehicle not found."))
                                .response(responseBuilder().responseCode("401")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Unauthorized"))
                                .summary("Returns location and driving mode.")
                                .description("Returns information about the vehicles whereabouts and if it's driving or parked.")
                                .security(securityRequirementBuilder().name("bearerAuth")))

                .GET("/api/1/vehicles/{id}/data_request/vehicle_state",
                        this::getVehicleState, ops -> ops.beanClass(VehicleService.class).beanMethod("getVehicleState")
                                .operationId("vehicle_state")
                                .response(responseBuilder().responseCode("200"))
                                .response(responseBuilder().responseCode("404")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Vehicle not found."))
                                .response(responseBuilder().responseCode("401")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Unauthorized"))
                                .summary("Door and look status")
                                .description("Returns information about the vehicles doors, if they are open or closed and if they are looked or unlocked.")
                                .security(securityRequirementBuilder().name("bearerAuth")))

                .POST("/api/1/vehicles/{id}/command/door_lock",
                        this::lockDoors, ops -> ops.beanClass(VehicleService.class).beanMethod("lockDoors")
                                .operationId("door_lock")
                                .response(responseBuilder().responseCode("200"))
                                .response(responseBuilder().responseCode("404")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Vehicle not found."))
                                .response(responseBuilder().responseCode("401")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Unauthorized"))
                                .summary("Locks the doors.")
                                .security(securityRequirementBuilder().name("bearerAuth")))

                .POST("/api/1/vehicles/{id}/command/door_unlock",
                        this::unLockDoors, ops -> ops.beanClass(VehicleService.class).beanMethod("unLockDoors")
                                .operationId("door_unlock")
                                .response(responseBuilder().responseCode("200"))
                                .response(responseBuilder().responseCode("404")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Vehicle not found."))
                                .response(responseBuilder().responseCode("401")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Unauthorized"))
                                .summary("Unlocks the doors.")
                                .security(securityRequirementBuilder().name("bearerAuth")))

                .GET("/api/1/vehicles",
                        this::getVehicles, ops -> ops.beanClass(VehicleService.class).beanMethod("getVehicles")
                                .operationId("vehicles")
                                .security(securityRequirementBuilder().name("bearerAuth")))

                .GET("/api/1/vehicles/{id}/data_request/climate_state",
                        this::getClimateState, ops -> ops.beanClass(VehicleService.class).beanMethod("getClimateState")
                                .operationId("climate_state")
                                .response(responseBuilder().responseCode("200").description("This is normal response for ids retrieved from vehicles endpoint"))
                                .response(responseBuilder().responseCode("404")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Vehicle not found."))
                                .response(responseBuilder().responseCode("401")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Unauthorized"))
                                .summary("Climate system status")
                                .description("Returns information about the vehicles selected settings for the climate system including actual temperature inside and outside.")
                                .security(securityRequirementBuilder().name("bearerAuth")))

                .POST("/api/1/vehicles/{id}/command/set_temps",
                        this::setTemperature, ops -> ops.beanClass(VehicleService.class)
                                .beanMethod("setTemperature")
                                .operationId("set_temps")
                                .parameter(parameterBuilder().name("driver_temp").description("Temperature for driver side in °C."))
                                .parameter(parameterBuilder().name("passenger_temp").description("Temperature for passenger side in °C."))
                                .response(responseBuilder().responseCode("200").description("This is the normal response"))
                                .response(responseBuilder().responseCode("400")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Bad request returned when missing parameters or when values are outside min/max range."))
                                .response(responseBuilder().responseCode("401")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Unauthorized"))
                                .security(securityRequirementBuilder().name("bearerAuth")))
                .build();
    }

    private Mono<ServerResponse> getClimateState(ServerRequest request) {
        if (Math.random() > 0.95) {
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double response = Math.random();
            if (response < 0.2)
                return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            if (response < 0.4)
                return status(HttpStatus.TOO_MANY_REQUESTS).build();
            if (response < 0.8)
                return status(HttpStatus.GATEWAY_TIMEOUT).build();

            return status(HttpStatus.I_AM_A_TEAPOT).build();
        }

        return vehicleService.getClimateState(Long.parseLong(request.pathVariable("id")))
                .flatMap(result -> ok().bodyValue(result))
                .switchIfEmpty(notFound().build());
    }

    private Mono<ServerResponse> setTemperature(ServerRequest request) {

        float driver_temp = request.queryParam("driver_temp")
                .filter(Predicate.not(String::isEmpty))
                .map(Float::valueOf)
                .orElse(-1f);
        float passenger_temp = request.queryParam("passenger_temp")
                .filter(Predicate.not(String::isEmpty))
                .map(Float::valueOf)
                .orElse(-1.0f);
        return vehicleService.setTemperature(Long.parseLong(request.pathVariable("id")), driver_temp, passenger_temp)
                .flatMap(result -> ok().bodyValue(result))
                .switchIfEmpty(badRequest().build());
    }

    private Mono<ServerResponse> getVehicles(ServerRequest request) {
        return ok().body(fromPublisher(vehicleService.getVehicles(), Vehicle.class));
    }

    private Mono<ServerResponse> getVehicleState(ServerRequest request) {
        return vehicleService.getVehicleState(Long.parseLong(request.pathVariable("id")))
                .flatMap(result -> ok().bodyValue(result))
                .switchIfEmpty(notFound().build());
    }

    private Mono<ServerResponse> getDriveState(ServerRequest request) {
        return vehicleService.getDriveState(Long.parseLong(request.pathVariable("id")))
                .flatMap(result -> ok().bodyValue(result))
                .switchIfEmpty(notFound().build());
    }

    private Mono<ServerResponse> lockDoors(ServerRequest request) {
        return ok()
                .body(vehicleService.lockDoors(Long.parseLong(request.pathVariable("id"))), Response.class);
    }

    private Mono<ServerResponse> unLockDoors(ServerRequest request) {
        return ok()
                .body(vehicleService.unLockDoors(Long.parseLong(request.pathVariable("id"))), Response.class);
    }
}
