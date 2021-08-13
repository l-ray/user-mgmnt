package de.lray.service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
        // hacky way to include the base path given by the AS into swagger urls
        servers = { @Server(url="http:///user")},
        info = @Info(
                title = "User Management API",
                version = "0.1",
                description = "This is the is a template for a service built with Micronaut",
                contact = @Contact(
                        email = "info@l-ray.de"
                )
        )
)
@ApplicationPath("/api")
public class JaxrsActivator extends Application {
}
