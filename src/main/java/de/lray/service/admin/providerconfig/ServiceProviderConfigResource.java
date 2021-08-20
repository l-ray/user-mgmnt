package de.lray.service.admin.providerconfig;

import de.lray.service.admin.providerconfig.dto.ServiceProviderConfig;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.slf4j.LoggerFactory;


@Path("/scim/v2/ServiceProviderConfig")
public class ServiceProviderConfigResource {

    public static final String SCIM_MEDIA_TYPE = "application/scim+json";

    @GET
    @Produces(SCIM_MEDIA_TYPE)
    public ServiceProviderConfig createServiceProviderConfig() {

        LoggerFactory.getLogger(ServiceProviderConfigResource.class)
                .info("Called Service Provider Config Controller");
        return new ServiceProviderConfig();
    }


}
