package de.lray.service.admin.user.it;

import de.lray.service.admin.ScimTestMessageFactory;
import de.lray.service.admin.providerconfig.ServiceProviderConfigResource;
import de.lray.service.admin.common.Meta;
import de.lray.service.admin.common.Error;
import de.lray.service.admin.user.*;
import de.lray.service.admin.user.dto.*;
import de.lray.service.admin.user.endpoint.*;
import de.lray.service.JaxrsActivator;
import de.lray.service.admin.user.operation.UserPatchOpField;
import de.lray.service.admin.user.persistence.UserRepository;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
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
public class UserAdminResourceTest {
    private final static Logger LOGGER = Logger.getLogger(UserAdminResourceTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(UserResult.class, UserResultItem.class, UserResource.class, Meta.class)
                .addClasses(UserAdd.class, UserPatch.class, UserPatchOp.class, UserName.class, UserEmail.class, UserPhone.class, UserPatchOpField.class)
                .addClass(UserSearchCriteria.class)
                .addClasses(UserRepository.class, MockedUserRepository.class, ScimTestMessageFactory.class)
                .addClasses(UserAlreadyExistsException.class, UserUnknownException.class)
                .addClasses(UserAlreadyExistsExceptionMapper.class, UserUnknownExceptionMapper.class, Error.class, ConstraintViolationExceptionMapper.class)
                .addClasses(UserAdminApi.class, UserAdminResource.class, JaxrsActivator.class);
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
    @DisplayName("User list should return no results.")
    void should_return_empty_user_list() throws MalformedURLException {
        LOGGER.info(" client: "+client+", baseURL: "+base);
        final var userTarget = this.client.target(new URL(this.base, "api/scim/v2/Users").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .get()) {
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.readEntity(UserResult.class).Resources).isEmpty();
        }
    }

    @Test
    @DisplayName("Single user should return result.")
    void should_return_single_user() throws MalformedURLException {
        LOGGER.info(" client: "+client+", baseURL: "+base);
        final var userTarget = this.client.target(new URL(this.base, "api/scim/v2/Users/123").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .get()) {
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.readEntity(UserResource.class).id).isEqualTo(ScimTestMessageFactory.EXAMPLE_ID);
        }
    }

    @Test
    @DisplayName("Adding user should return error.")
    void should_return_not_found_error_on_user_add() throws MalformedURLException {
        LOGGER.info(" client: "+client+", baseURL: "+base);
        final var userTarget = this.client.target(new URL(this.base, "api/scim/v2/Users").toExternalForm());

        var payload = ScimTestMessageFactory.createUserAdd();
        payload.password = "123";

        try (final Response response = userTarget.request(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .post(json(payload))) {

            assertThat(response.getStatus()).isEqualTo(409);
            var responseEntity = response.readEntity(Error.class);
            assertThat(responseEntity.detail).contains("already exists");
            assertThat(responseEntity.status).isEqualTo(409);
            assertThat(responseEntity.schemas).hasSize(1).first().asString().contains("scim");
        }
    }

    @Test
    @DisplayName("Updating user should return object.")
    void should_return_object_on_update() throws MalformedURLException {
        LOGGER.info(" client: "+client+", baseURL: "+base);
        final var userTarget = this.client.target(new URL(this.base, "api/scim/v2/Users/3").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .put(json(ScimTestMessageFactory.createUserAdd()))) {

            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.readEntity(UserResource.class).id).isEqualTo(ScimTestMessageFactory.EXAMPLE_ID);
        }
    }

    @Test
    @DisplayName("Patching user should return object.")
    void should_return_error_on_patch() throws MalformedURLException {
        LOGGER.info(" client: "+client+", baseURL: "+base);
        final var userTarget = this.client.target(new URL(this.base, "api/scim/v2/Users/3").toExternalForm());
        //final var requestObject = ScimTestMessageFactory.createUserPatch();
        final var requestObject = "{\"schemas\": [\"urn:ietf:params:scim:api:messages:2.0:PatchOp\"],\"Operations\": []}";

        final var requestEntity = Entity.json(requestObject);
        try (final Response response = userTarget.request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .property("jersey.config.client.httpUrlConnection.setMethodWorkaround", true)
                .method("PATCH",requestEntity)) {

            assertThat(response.getStatus()).isEqualTo(404);
            var responseEntity = response.readEntity(Error.class);
            assertThat(responseEntity.detail).contains("not found");
            assertThat(responseEntity.status).isEqualTo(404);
            assertThat(responseEntity.schemas).hasSize(1).first().asString().contains("scim");
        }
    }
}

