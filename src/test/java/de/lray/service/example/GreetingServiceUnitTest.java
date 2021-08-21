package de.lray.service.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GreetingServiceUnitTest {

    GreetingService service;

    @BeforeEach
    void setup() {
        service = new GreetingService();
    }

    @Test
    void testGreeting(){
       var message = service.buildGreetingMessage("JakartaEE");
       assertThat(message.getMessage()).startsWith("Say Hello to JakartaEE");
    }
}
