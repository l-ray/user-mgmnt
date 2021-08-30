package de.lray.service.admin.providerconfig;

import de.lray.service.admin.providerconfig.dto.ServiceProviderConfig;
import de.lray.service.admin.providerconfig.endpoint.ServiceProviderConfigApi;
import org.slf4j.LoggerFactory;

public class ServiceProviderConfigResource implements ServiceProviderConfigApi {

    public static final String SCIM_MEDIA_TYPE = "application/scim+json";

    @Override
    public ServiceProviderConfig createServiceProviderConfig() {
        LoggerFactory.getLogger(ServiceProviderConfigResource.class)
                .info("Called Service Provider Config Controller");
        return new ServiceProviderConfig();
    }
}
