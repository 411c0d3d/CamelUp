package com.oasisstudios.camelupserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The CamelDOM up server application.
 */
@SpringBootApplication
public class CamelUpServerApplication {

    /**
     * The entry point of the application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CamelUpServerApplication.class, args);
    }

}