package de.lray.service.admin.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;
import java.util.StringJoiner;

@SuppressWarnings("java:S1104")
public class UserAdd extends UserResource {

  @NotEmpty
  public String password = null;

  @JsonProperty(value = "urn:okta:onprem_app:1.0:user:custom")
  @JsonbProperty(value = "urn:okta:onprem_app:1.0:user:custom")
  public static final Map<String, Object> custom  = Map.of(
      "isAdmin", false,
      "isOkta", true,
      "departmentName", "Cloud Service"
  );

  @Override
  public String toString() {
    return new StringJoiner(", ", UserAdd.class.getSimpleName() + "[", "]")
            .add("password='" + password + "'")
            .add("custom=" + custom)
            .add("displayName='" + displayName + "'")
            .add("preferredLanguage='" + preferredLanguage + "'")
            .add("locale='" + locale + "'")
            .add("timezone='" + timezone + "'")
            .add("schemas=" + schemas)
            .add("id='" + id + "'")
            .add("userName='" + userName + "'")
            .add("active=" + active)
            .add("name=" + name)
            .add("emails=" + emails)
            .add("phoneNumbers=" + phoneNumbers)
            .add("roles=" + roles)
            .add("meta=" + meta)
            .toString();
  }
}
