package de.lray.service.admin.providerconfig;

import de.lray.service.admin.providerconfig.dto.ServiceProviderConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ServiceProviderConfigResourceTest {

  @Test
  public void whenCalled_thenReturned() {

    // When
    ServiceProviderConfig response = null;
    response = new ServiceProviderConfigResource().createServiceProviderConfig();
    // Then

    Assertions.assertThat(response)
            .isNotNull()
            .isInstanceOf(ServiceProviderConfig.class);
  }
}