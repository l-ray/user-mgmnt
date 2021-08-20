package de.lray.service.admin.providerconfig.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.json.bind.annotation.JsonbProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S1104")
public class ServiceProviderConfig {

    public static final List<String> schemas = List.of(
            "urn:ietf:params:scim:schemas:core:2.0:ServiceProviderConfig"
    );
    public static final String SUPPORTED = "supported";
    public String documentationUrl = "https://support.okta.com/scim-fake-page.html";
    public List<String> authenticationSchemes = Arrays.asList();
    public Map<String, Boolean> patch = Map.of(SUPPORTED, true);
    public Map<String, Boolean> bulk = Map.of(SUPPORTED, false);
    public Map<String, Object> filter = Map.of(SUPPORTED, true, "maxResults", 100);
    public Map<String, Boolean> changePassword = Map.of(SUPPORTED, true);
    public Map<String, Boolean> sort = Map.of(SUPPORTED, false);
    public Map<String, Boolean> etag = Map.of(SUPPORTED, false);


    @JsonbProperty(value = "urn:okta:schemas:scim:providerconfig:1.0")
    @JsonProperty(value = "urn:okta:schemas:scim:providerconfig:1.0")
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