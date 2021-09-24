package se.iths.remoteyourcar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RemoteYourCarApplication {

    public static void main(String[] args) {
       // BlockHound.install();
        SpringApplication.run(RemoteYourCarApplication.class, args);
    }

}
