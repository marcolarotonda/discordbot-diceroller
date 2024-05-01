package io.github.marcolarotonda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationDiceRoller {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApplicationDiceRoller.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run();
    }

}
