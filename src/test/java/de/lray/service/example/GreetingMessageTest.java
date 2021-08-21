package de.lray.service.example;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class GreetingMessageTest {

    @Test
    void testGreetingMessage() {
        var message = GreetingMessage.of("Say Hello to JatartaEE");
        assertThat(message.getMessage()).isEqualTo("Say Hello to JatartaEE");
    }
}
