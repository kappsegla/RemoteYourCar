package se.iths.remoteyourcar.routing;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import se.iths.remoteyourcar.entities.DoorState;
import se.iths.remoteyourcar.repositories.VehicleRepository;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
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
                        this::unLockDoors, ops -> ops.beanClass(VehicleService.class).beanMethod("unLockDoors")).build();
    }

    private Mono<ServerResponse> getVehicleState(ServerRequest request) {
        return vehicleService.getVehicleState(Long.parseLong(request.pathVariable("id")))
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    private Mono<ServerResponse> getDriveState(ServerRequest request) {
        return vehicleService.getDriveState(Long.parseLong(request.pathVariable("id")))
                .flatMap(result -> ServerResponse.ok().bodyValue(result))
                .switchIfEmpty(ServerResponse.notFound().build());
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
