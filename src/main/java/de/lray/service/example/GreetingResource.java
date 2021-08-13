package de.lray.service.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class GreetingResource implements GreetingApi {

    @Inject
    private GreetingService greetingService;

    @Override
    public GreetingMessage greeting(String name) {
        return this.greetingService.buildGreetingMessage(name);
    }
}
