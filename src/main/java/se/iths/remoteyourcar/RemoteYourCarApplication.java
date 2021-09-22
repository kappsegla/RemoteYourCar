package se.iths.remoteyourcar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class RemoteYourCarApplication {

    public static void main(String[] args) {
       // BlockHound.install();
        SpringApplication.run(RemoteYourCarApplication.class, args);
    }

}
