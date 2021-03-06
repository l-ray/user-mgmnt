package de.lray.service.admin.providerconfig.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S1104")
public class ServiceProviderConfig {

    public final List<String> schemas = List.of(
            "urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig"
    );
    public static final String SUPPORTED = "supported";
    public String documentationUrl = "https://github.com/l-ray/user-mgmnt";
    public List<String> authenticationSchemes = Arrays.asList();
    public Map<String, Boolean> patch = Map.of(SUPPORTED, true);
    public Map<String, Boolean> bulk = Map.of(SUPPORTED, false);
    public Map<String, Object> filter = Map.of(SUPPORTED, true, "maxResults", 100);
    public Map<String, Boolean> changePassword = Map.of(SUPPORTED, true);
    public Map<String, Boolean> sort = Map.of(SUPPORTED, false);
    public Map<String, Boolean> etag = Map.of(SUPPORTED, false);

    // work-around for https://github.com/swagger-api/swagger-core/issues/2789
    @Schema(name = "urn:okta:schemas:scim:providerconfig:1.0")
    @JsonbProperty(value = "urn:okta:schemas:scim:providerconfig:1.0")
    public Map<String, List<String>> providerConfig = Map.of(
            "userManagementCapabilities", Arrays.asList(
                    "IMPORT_NEW_USERS",
                    "IMPORT_PROFILE_UPDATES",
                    "PUSH_NEW_USERS",
                    "PUSH_PASSWORD_UPDATES",
                    "PUSH_PENDING_USERS",
                    "PUSH_PROFILE_UPDATES",
                    "PUSH_USER_DEACTIVATION",
                    "REACTIVATE_USERS"
            )
    );
}