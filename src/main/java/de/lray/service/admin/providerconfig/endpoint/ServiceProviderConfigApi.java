package de.lray.service.admin.providerconfig.endpoint;

import de.lray.service.admin.providerconfig.ServiceProviderConfigResource;
import de.lray.service.admin.providerconfig.dto.ServiceProviderConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Tag(name = "Service Provider Configuration", description = "SCIM v2 configuration for user management endpoint.")
@SecurityScheme(name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@Path("/scim/v2/ServiceProviderConfig")
public interface ServiceProviderConfigApi {

    @Operation(summary = "Service Provider Configuration", description = "Configuration and capabilities.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Configuration.", content = {
                    @Content(
                            mediaType = ServiceProviderConfigResource.SCIM_MEDIA_TYPE,
                            schema = @Schema(
                                    type = "object",
                                    implementation = ServiceProviderConfig.class
                            )
                    )
            }
            )
    })
    @GET
    @Produces(ServiceProviderConfigResource.SCIM_MEDIA_TYPE)
    ServiceProviderConfig createServiceProviderConfig();
}
