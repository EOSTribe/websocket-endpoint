package eosio.spectrum.websocket.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("eosio.spectrum.websocket.api")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
