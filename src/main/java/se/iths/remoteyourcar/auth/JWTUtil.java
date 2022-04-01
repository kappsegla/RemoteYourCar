package se.iths.remoteyourcar.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

@Component
public class JWTUtil {

    @Value("${JWT_SECRET}")
    private String key;

    public Jws<Claims> getAllClaimsFromToken(String authToken) {
        byte[] bytes = Base64.getDecoder().decode(key.getBytes());

        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf;
        PublicKey pub = null;
        try {
            kf = KeyFactory.getInstance("EC");
            pub = kf.generatePublic(ks);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return  Jwts.parserBuilder()
                .setSigningKey(pub)
                .build()
                .parseClaimsJws(authToken);
    }
}
