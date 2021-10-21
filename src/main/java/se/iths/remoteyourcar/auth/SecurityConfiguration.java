package se.iths.remoteyourcar.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {

        // Disable default security.
        return http.httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable()
                //No session
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                // Add custom security.
                .authenticationManager(this.authenticationManager)
                .securityContextRepository(this.securityContextRepository)
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() ->
                        swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
                )).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() ->
                        swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))).and()
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET,"/api/1/vehicles").authenticated()
                .pathMatchers(HttpMethod.GET,"/api/1/vehicles/*/data_request/drive_state").authenticated()
                .pathMatchers(HttpMethod.GET,"/api/1/vehicles/*/data_request/vehicle_state").authenticated()
                .pathMatchers(HttpMethod.GET,"/api/1/vehicles/*/data_request/climate_state").authenticated()
                .pathMatchers(HttpMethod.POST,"/api/1/vehicles/*/command/door_lock").authenticated()
                .pathMatchers(HttpMethod.POST,"/api/1/vehicles/*/command/door_unlock").authenticated()
                .anyExchange().permitAll()
                .and().build();
    }
}