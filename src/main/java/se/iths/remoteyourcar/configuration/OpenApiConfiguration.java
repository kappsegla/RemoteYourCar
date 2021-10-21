package se.iths.remoteyourcar.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    static record UserInfo(String username, String password) {}
    static record TokenResponse(String access_token,String token_type, int expires_in){}

    @Bean
    public OpenAPI springOpenApi() {
        final String securitySchemeName = "bearerAuth";
        Schema schema = new Schema()
                .title("UserInfo")
                .addProperties("username", new StringSchema())
                .addProperties("password", new StringSchema())
                .example(new UserInfo("test@test.nu", "pass"));

        Schema response = new Schema()
                .addProperties("access_token", new StringSchema())
                .addProperties("token_type", new StringSchema())
                .addProperties("expires_in", new IntegerSchema())
                .example(new TokenResponse("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrYWxsZS5hbmthQGFua2Vib3JnLm51IiwiaWF0IjoxNjM0ODM3OTk0LCJleHAiOjE2MzQ5MjQzOTQsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJ2ZWhpY2xlcyI6WyIzIiwiNCJdfQ.gMPj3tqIseV6LbFWzbObKO9IoPZT5WNRIMXFlz-b8fzwENcYcJzPnoJtc6rw2Q_7XQPLjw-QHXjBtk2H5II8Ag","Bearer ",86400));

        return new OpenAPI()
                .info(new Info().title("Remote Your Car API")
                        .description("Api for commands and queries against a simulated car.")
                        .version("v0.0.1"))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .path("/sign-up/",
                        new PathItem().post(
                                new Operation()
                                        .description("Create new user account")
                                        .tags(List.of("auth"))
                                        .requestBody(new RequestBody()
                                                .required(true)
                                                .content(new Content()
                                                        .addMediaType("application/json",
                                                                new MediaType().schema(schema)))
                                        ).responses(new ApiResponses()
                                                .addApiResponse("200", new ApiResponse().description("200 OK"))
                                                .addApiResponse("409", new ApiResponse().description("Conflict")))
                        ))
                .path("/auth/",
                        new PathItem().post(
                                new Operation()
                                        .description("Retrieve token")
                                        .tags(List.of("auth"))
                                        .requestBody(new RequestBody()
                                                .required(true)
                                                .content(new Content()
                                                        .addMediaType("application/json",
                                                                new MediaType().schema(schema)))
                                        ).responses(new ApiResponses()
                                                .addApiResponse("200", new ApiResponse().description("OK")
                                                        .content(new Content()
                                                                .addMediaType("application/json",
                                                                        new MediaType().schema(response))))
                                                .addApiResponse("401", new ApiResponse().description("Unauthorized")))

                        ));
    }
}
