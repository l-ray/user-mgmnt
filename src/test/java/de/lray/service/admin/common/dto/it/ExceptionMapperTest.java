package de.lray.service.admin.common.dto.it;

import de.lray.service.JaxrsActivator;
import de.lray.service.admin.common.dto.Error;
import de.lray.service.admin.user.endpoint.*;
import de.lray.service.admin.user.exception.UserAlreadyExistsException;
import de.lray.service.admin.user.exception.UserCreationException;
import de.lray.service.admin.user.exception.UserUnknownException;
import de.lray.service.admin.user.dto.UserName;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import static jakarta.ws.rs.client.Entity.json;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ArquillianExtension.class)
@RunAsClient
public class ExceptionMapperTest {

    private final static Logger LOGGER = Logger.getLogger(ExceptionMapperTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(UserName.class, TestMessage.class)
                .addClasses(UserAlreadyExistsException.class, UserUnknownException.class, UserCreationException.class)
                .addClasses(UserAlreadyExistsExceptionMapper.class, UserUnknownExceptionMapper.class, Error.class,
                        ConstraintViolationExceptionMapper.class, ValidationExceptionMapper.class,
                        UserCreationExceptionMapper.class)
                .addClasses(ErrorTestingApi.class, JaxrsActivator.class);
    }

    @ArquillianResource
    private URL base;

    private Client client;

    @BeforeEach
    void setup() {
        LOGGER.info("call BeforeEach");
        this.client = ClientBuilder.newClient();
    }

    @AfterEach
    void teardown() {
        LOGGER.info("call AfterEach");
        if (this.client != null) {
            this.client.close();
        }
    }

    @Test
    @DisplayName("Simple call should return result.")
    void should_return_result() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var target = this.client.target(new URL(this.base, "api/errorTest/ping").toExternalForm());
        try (final Response response = target.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {
            assertThat(response.readEntity(TestMessage.class).message).isEqualTo("pong");
            assertThat(response.getStatus()).isEqualTo(200);
        }
    }

    @Test
    @DisplayName("Simple post should return result.")
    void post_should_return_result() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var target = this.client.target(new URL(this.base, "api/errorTest/ping").toExternalForm());
        var payload = new UserName();
        payload.familyName = "Johnson";
        try (final Response response = target.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(json(payload))) {

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.readEntity(TestMessage.class).message).isEqualTo("pong");
        }
    }

    @Test
    @DisplayName("Should return Unknown User Error.")
    void should_return_not_found_error() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var userTarget = this.client.target(new URL(this.base, "api/errorTest/unknownUser").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {

            assertScimErrorResponseWithStatusAndDetail(response, 404, "unknown user");
        }
    }

    @Test
    @DisplayName("Should return User Creation Error.")
    void should_return_user_creation_error() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var userTarget = this.client.target(new URL(this.base, "api/errorTest/userCreationProblem").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {

            assertScimErrorResponseWithStatusAndDetail(response, 400, "user creation");
        }
    }


    @Test
    @DisplayName("Should return already exist User Error.")
    void should_return_already_exist_error() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var userTarget = this.client.target(new URL(this.base, "api/errorTest/alreadyExist").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {

            assertScimErrorResponseWithStatusAndDetail(response, 409, "already exists");
        }
    }

    @Test
    @DisplayName("Should return violation Error.")
    void should_return_violation_error() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var userTarget = this.client.target(new URL(this.base, "api/errorTest/violationError").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {

            assertScimErrorResponseWithStatusAndDetail(response, 400, "");
        }
    }

    @Test
    @DisplayName("Should return validation Error.")
    void should_return_validation_error() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var userTarget = this.client.target(new URL(this.base, "api/errorTest/validationError").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {

            assertScimErrorResponseWithStatusAndDetail(response, 400, "");
        }
    }

    private void assertScimErrorResponseWithStatusAndDetail(Response response, int httpStatus, String details) {
        var responseEntity = response.readEntity(Error.class);
        assertThat(responseEntity.detail).contains(details);
        assertThat(responseEntity.status).isEqualTo(httpStatus);
        assertThat(responseEntity.schemas).hasSize(1).first().asString().contains("scim");
        assertThat(response.getStatus()).isEqualTo(httpStatus);
    }

}
