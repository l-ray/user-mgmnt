package de.lray.service.admin.common.it;

import de.lray.service.JaxrsActivator;
import de.lray.service.admin.common.Error;
import de.lray.service.admin.user.UserAlreadyExistsException;
import de.lray.service.admin.user.UserUnknownException;
import de.lray.service.admin.user.dto.UserName;
import de.lray.service.admin.user.endpoint.ConstraintViolationExceptionMapper;
import de.lray.service.admin.user.endpoint.UserAlreadyExistsExceptionMapper;
import de.lray.service.admin.user.endpoint.UserUnknownExceptionMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
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
                .addClasses(UserAlreadyExistsException.class, UserUnknownException.class)
                .addClasses(UserAlreadyExistsExceptionMapper.class, UserUnknownExceptionMapper.class, Error.class, ConstraintViolationExceptionMapper.class)
                .addClasses(ErrorTestingApi.class, JaxrsActivator.class);
    }

    @ArquillianResource
    private URL base;

    private Client client;

    @BeforeEach
    public void setup() {
        LOGGER.info("call BeforeEach");
        this.client = ClientBuilder.newClient();
    }

    @AfterEach
    public void teardown() {
        LOGGER.info("call AfterEach");
        if (this.client != null) {
            this.client.close();
        }
    }

    @Test
    @DisplayName("Simple call should return result.")
    public void should_return_result() throws MalformedURLException {
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
    public void post_should_return_result() throws MalformedURLException {
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
    public void should_return_not_found_error() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var userTarget = this.client.target(new URL(this.base, "api/errorTest/unknownUser").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {

            var responseEntity = response.readEntity(Error.class);
            assertThat(responseEntity.detail).contains("unknown user");
            assertThat(responseEntity.status).isEqualTo(404);
            assertThat(responseEntity.schemas).hasSize(1).first().asString().contains("scim");
            assertThat(response.getStatus()).isEqualTo(404);
        }
    }

    @Test
    @DisplayName("Should return already exist User Error.")
    public void should_return_already_exist_error() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var userTarget = this.client.target(new URL(this.base, "api/errorTest/alreadyExist").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {

            var responseEntity = response.readEntity(Error.class);
            assertThat(responseEntity.detail).contains("already exists");
            assertThat(responseEntity.status).isEqualTo(409);
            assertThat(responseEntity.schemas).hasSize(1).first().asString().contains("scim");
            assertThat(response.getStatus()).isEqualTo(409);
        }
    }

    @Test
    @DisplayName("Should return validation Error.")
    public void should_return_validation_error() throws MalformedURLException {
        LOGGER.info(" client: " + client + ", baseURL: " + base);
        final var userTarget = this.client.target(new URL(this.base, "api/errorTest/validationError").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()) {

            var responseEntity = response.readEntity(Error.class);
            assertThat(responseEntity.detail).contains("");
            assertThat(responseEntity.status).isEqualTo(400);
            assertThat(responseEntity.schemas).hasSize(1).first().asString().contains("scim");
            assertThat(response.getStatus()).isEqualTo(400);
        }
    }

}
