package se.iths.remoteyourcar.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;

    public AuthenticationManager(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        try {
            //Check if signed with our secret key
            var claims = jwtUtil.getAllClaimsFromToken(authToken);
            if (claims == null) {
                return Mono.empty();
            }

            //Get list of roles for this user
            List<String> perms = (List<String>) claims.getBody().get("authorities");

            var authorities = perms.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            var creds = (List<String>) claims.getBody().get("vehicles");

            var credentials = creds.stream().map(Long::valueOf).toList();

            return Mono.just(new UsernamePasswordAuthenticationToken(claims.getBody().getSubject(), credentials, authorities));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}