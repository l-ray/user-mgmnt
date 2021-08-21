package de.lray.service.admin.providerconfig.it;

import de.lray.service.JaxrsActivator;
import de.lray.service.admin.common.Error;
import de.lray.service.admin.providerconfig.ServiceProviderConfigResource;
import de.lray.service.admin.providerconfig.dto.ServiceProviderConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ArquillianExtension.class)
@RunAsClient
public class ServiceProviderConfigResourceTest {
    private final static Logger LOGGER = Logger.getLogger(ServiceProviderConfigResourceTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(ServiceProviderConfig.class)
                .addClasses(Error.class)
                .addClasses(ServiceProviderConfigResource.class, JaxrsActivator.class);
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
    @DisplayName("Service provider config should return results.")
    void should_return_service_provider_config() throws MalformedURLException {
        LOGGER.info(" client: "+client+", baseURL: "+base);
        final var userTarget = this.client.target(new URL(this.base, "api/scim/v2/ServiceProviderConfig").toExternalForm());
        try (final Response response = userTarget.request()
                .accept(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
                .get()) {
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(
                    response.readEntity(ServiceProviderConfig.class).schemas.get(0))
                    .isEqualTo("urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig");
        }
    }
}

