package se.iths.remoteyourcar.routing;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.fn.builders.parameter.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.Response;
import se.iths.remoteyourcar.entities.DoorState;
import se.iths.remoteyourcar.entities.Vehicle;

import java.util.List;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration(proxyBeanMethods = false)
public class VehicleRouter {

    //https://tesla-api.timdorr.com/
    //https://www.teslaapi.io/


    private final VehicleService vehicleService;

    public VehicleRouter(VehicleService handler) {
        this.vehicleService = handler;
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Remote Your Car API")
                        .description("Api for commands and queries against a simulated car.")
                        .version("v0.0.1"));
//                .externalDocs(new ExternalDocumentation()
//                        .description("SpringShop Wiki Documentation")
//                        .url("https://springshop.wiki.github.org/docs"));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route()
                .GET("/api/1/vehicles/{id}/data_request/drive_state",
                        this::getDriveState, ops -> ops
                                .beanClass(VehicleService.class)
                                .beanMethod("getDriveState")
                                .operationId("drive_state")
                                .response(responseBuilder().responseCode("200").description("This is normal response for id 1-100"))
                                .response(responseBuilder().responseCode("404")
                                        .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Vehicle not found."))
                                .summary("Returns location and driving mode.")
                                .description("Returns information about the vehicles whereabouts and if it's driving or parked."))

                .GET("/api/1/vehicles/{id}/data_request/vehicle_state",
                        this::getVehicleState, ops -> ops.beanClass(VehicleService.class).beanMethod("getVehicleState"))

                .POST("/api/1/vehicles/{id}/command/door_lock",
                        this::lockDoors, ops -> ops.beanClass(VehicleService.class).beanMethod("lockDoors"))

                .POST("/api/1/vehicles/{id}/command/door_unlock",
                        this::unLockDoors, ops -> ops.beanClass(VehicleService.class).beanMethod("unLockDoors"))

                .GET("/api/1/vehicles",
                        this::getVehicles, ops -> ops.beanClass(VehicleService.class).beanMethod("getVehicles"))

                .GET("/api/1/vehicles/{id}/data_request/climate_state",
                        this::getClimateState, ops -> ops.beanClass(VehicleService.class).beanMethod("getClimateState"))
                //?driver_temp=:driver_temp&passenger_temp=:passenger_temp
                .POST("/api/1/vehicles/{id}/command/set_temps",
                        this::setTemperature, ops -> ops.beanClass(VehicleService.class)
                                .beanMethod("setTemperature")
                                .operationId("set_temp")
                                .parameter(parameterBuilder().name("driver_temp").description("Temperature for driver side."))
                                .parameter(parameterBuilder().name("passenger_temp").description("Temperature for passenger side."))
                                .response(responseBuilder().responseCode("200").description("This is the normal response"))
                                .response(responseBuilder().responseCode("400")
                                            .content(contentBuilder().schema(schemaBuilder().hidden(true))).description("Bad request returned when missing parameters or when values are outside min/max range.")))
                .build();
    }

    private Mono<ServerResponse> getClimateState(ServerRequest request) {
        return vehicleService.getClimateState(Long.parseLong(request.pathVariable("id")))
                .flatMap(result -> ok().bodyValue(result))
                .switchIfEmpty(notFound().build());
    }

    private Mono<ServerResponse> setTemperature(ServerRequest request) {

        float driver_temp = request.queryParam("driver_temp")
                .map(Float::valueOf)
                .orElse(-1f);
        float passenger_temp = request.queryParam("passenger_temp")
                .map(Float::valueOf)
                .orElse(-1f);

        return ok()
                .body(vehicleService.setTemperature(Long.parseLong(request.pathVariable("id")), driver_temp, passenger_temp), Response.class);
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
