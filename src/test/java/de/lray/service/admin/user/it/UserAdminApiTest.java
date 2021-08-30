package de.lray.service.admin.user.it;

import de.lray.service.JaxrsActivator;
import de.lray.service.admin.Resources;
import de.lray.service.admin.ScimTestMessageFactory;
import de.lray.service.admin.common.dto.Error;
import de.lray.service.admin.common.dto.Meta;
import de.lray.service.admin.providerconfig.ServiceProviderConfigResource;
import de.lray.service.admin.user.UserAdminResource;
import de.lray.service.admin.user.UserSearchCriteria;
import de.lray.service.admin.user.authentication.SimplePBKDF2Hasher;
import de.lray.service.admin.user.dto.UserAdd;
import de.lray.service.admin.user.dto.UserResource;
import de.lray.service.admin.user.dto.UserResult;
import de.lray.service.admin.user.endpoint.ConstraintViolationExceptionMapper;
import de.lray.service.admin.user.endpoint.UserAdminApi;
import de.lray.service.admin.user.endpoint.UserAlreadyExistsExceptionMapper;
import de.lray.service.admin.user.endpoint.UserUnknownExceptionMapper;
import de.lray.service.admin.user.exception.UserAlreadyExistsException;
import de.lray.service.admin.user.persistence.JdbcUserRepository;
import de.lray.service.admin.user.persistence.UserRepository;
import de.lray.service.admin.user.persistence.entities.User;
import de.lray.service.admin.user.persistence.mapper.UserToUserResourceMapper;
import de.lray.service.admin.user.persistence.mapper.UserToUserResultItemMapper;
import de.lray.service.admin.user.persistence.patch.UserPatchOpAction;
import de.lray.service.admin.user.validation.PasswordConstraints;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
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
public class UserAdminApiTest {
    private final static Logger LOGGER = Logger.getLogger(UserAdminApiTest.class.getName());
    public static final String ENDPOINT_URL = "api/scim/v2/Users/";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage(UserResult.class.getPackage())
                .addPackage(Meta.class.getPackage())
                .addPackage(UserPatchOpAction.class.getPackage())
                .addClass(UserSearchCriteria.class)
                .addPackage(JdbcUserRepository.class.getPackage())
                .addClasses(UserRepository.class, ScimTestMessageFactory.class)
                .addPackage(UserAlreadyExistsException.class.getPackage())
                .addClass(SimplePBKDF2Hasher.class)
                .addClasses(UserAlreadyExistsExceptionMapper.class, UserUnknownExceptionMapper.class, Error.class, ConstraintViolationExceptionMapper.class)
                .addClasses(UserAdminApi.class, UserAdminResource.class)
                .addClasses(JaxrsActivator.class)
                .addPackage(User.class.getPackage())
                .addPackage(PasswordConstraints.class.getPackage())
                .addClasses(UserToUserResultItemMapper.class, UserToUserResourceMapper.class)
                .addClass(Resources.class)
                .addClass(SimplePBKDF2Hasher.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URL base;

    private Client client;

   @BeforeEach
    void setup() throws Exception {
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
    @DisplayName("Adding minimal user should return it.")
    void should_successful_add_minimum_detailed_user() throws MalformedURLException {
        // Given
        final var userTarget = getTarget(ENDPOINT_URL);
        var payload = ScimTestMessageFactory.createMinimalUserAdd();

        // When
        try (final Response response = userTarget.request(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .post(json(payload))) {

            // Then
            assertThat(response.getStatus()).isEqualTo(200);
            var responseEntity = response.readEntity(UserAdd.class);
            assertThat(responseEntity.id).isNotNull();
        }
    }

    @Test
    @DisplayName("Adding user should return it.")
    void should_successful_add_user() throws MalformedURLException {
        // Given
        final var userTarget = getTarget(ENDPOINT_URL);
        var payload = ScimTestMessageFactory.createUserAdd();

        // When
        try (final Response response = userTarget.request(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .post(json(payload))) {

            // Then
            assertThat(response.getStatus()).isEqualTo(200);
            var responseEntity = response.readEntity(String.class);
            assertThat(responseEntity).contains("urn:lray:schemas:User:1.0:custom");
        }
    }

    @Test
    @DisplayName("User list should return results.")
    void should_return_user_list() throws MalformedURLException {
        // Given
        String expectedResultId;
        final var userTarget = getTarget(ENDPOINT_URL);
        var payload = ScimTestMessageFactory.createUserAdd();
        payload.userName = "user-listing-test";
        payload.id = null;
        try (final Response response = userTarget.request(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .post(json(payload))) {
            expectedResultId = response.readEntity(UserResource.class).id;
        }
        // When / Then
        try (final Response response = userTarget.request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .get()) {
            assertThat(response.getStatus()).isEqualTo(200);
            var result = response.readEntity(UserResult.class);
            assertThat(result.Resources).isNotEmpty();
            assertThat(result.Resources.stream().map((it) -> it.id)).contains(expectedResultId );
            assertThat(result.totalResults).isEqualTo(result.Resources.size());
        }
        // when called using user name filter
        try (final Response response = userTarget
                .queryParam("filter", "userName eq \"user-listing-test\"")
                .request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .get()) {
            assertThat(response.getStatus()).isEqualTo(200);
            var result = response.readEntity(UserResult.class);
            assertThat(result.Resources).isNotEmpty();
            assertThat(result.Resources.stream().map((it) -> it.id)).containsExactly(expectedResultId);
            assertThat(result.totalResults).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("Updating user should return updated object.")
    void should_return_object_on_update() throws MalformedURLException {
        // Given
        String expectedResultId;
        final var userTarget = getTarget(ENDPOINT_URL);
        var payload = ScimTestMessageFactory.createUserAdd();
        payload.userName = "user-update-test";
        payload.id = null;
        try (final Response response = userTarget.request(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .post(json(payload))) {
            expectedResultId = response.readEntity(UserResource.class).id;
        }
        // When / Then
        payload.id = null;
        payload.userName = "updated-user-name";
        try (final Response response = getTarget(ENDPOINT_URL + expectedResultId).request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .put(json(payload))) {

            assertThat(response.getStatus()).isEqualTo(200);
            var result = response.readEntity(UserResource.class);
            assertThat(result)
                    .hasFieldOrPropertyWithValue("userName","updated-user-name")
                    .hasFieldOrPropertyWithValue("id",expectedResultId);
        }
        // When called directly
        try (final Response response = getTarget(ENDPOINT_URL + expectedResultId).request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .get()) {

            assertThat(response.getStatus()).isEqualTo(200);
            var result = response.readEntity(UserResource.class);
            assertThat(result)
                    .hasFieldOrPropertyWithValue("userName","updated-user-name")
                    .hasFieldOrPropertyWithValue("id",expectedResultId);
        }
    }

    @Test
    @DisplayName("Patching user should return updated object.")
    void should_return_object_on_patch() throws MalformedURLException {
        // Given
        String expectedResultId;
        final var userTarget = getTarget(ENDPOINT_URL);
        var payload = ScimTestMessageFactory.createUserAdd();
        payload.userName = "user-patch-test";
        payload.id = null;
        try (final Response response = userTarget.request(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .post(json(payload))) {
            var result = response.readEntity(UserResource.class);
            expectedResultId = result.id;
            assertThat(result).hasFieldOrPropertyWithValue("active", true);
        }
        // When / Then
        var requestEntity = ScimTestMessageFactory.createUserPatch();
        try (final Response response = getTarget(ENDPOINT_URL + expectedResultId).request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .property("jersey.config.client.httpUrlConnection.setMethodWorkaround", true)
                .method("PATCH",json(requestEntity))) {

            assertThat(response.getStatus()).isEqualTo(200);
            var result = response.readEntity(UserResource.class);
            assertThat(result)
                    .hasFieldOrPropertyWithValue("active",false);
        }
        // When called directly
        try (final Response response = getTarget(ENDPOINT_URL + expectedResultId).request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .get()) {

            assertThat(response.getStatus()).isEqualTo(200);
            var result = response.readEntity(UserResource.class);
            assertThat(result)
                    .hasFieldOrPropertyWithValue("active",false);
        }
    }

    private WebTarget getTarget(String url) throws MalformedURLException {
        return this.client.target(new URL(this.base, url).toExternalForm());
    }
}
