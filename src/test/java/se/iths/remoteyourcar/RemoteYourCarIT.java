package se.iths.remoteyourcar;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteYourCarIT {

    @LocalServerPort
    private int port;

    @Test
    void smokeTestApiAvailable() {
       java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:"+port+"/api/1/vehicles/65/data_request/vehicle_state/"))
                .build();
        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        assertThat(response.statusCode()).isEqualTo(200);

        //assert response.body() is the right json with jsonassert?
    }
}
