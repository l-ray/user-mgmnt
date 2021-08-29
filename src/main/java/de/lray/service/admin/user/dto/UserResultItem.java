package de.lray.service.admin.user.dto;

import de.lray.service.admin.common.dto.Meta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("java:S1104")
public class UserResultItem {

    private static final String USER_EXTENSION_SCHEMA_ID = "urn:lray:schemas:User:1.0:custom";

    public final List<String> schemas =
            List.of("urn:ietf:params:scim:schemas:core:2.0:User",
                    USER_EXTENSION_SCHEMA_ID);

    public String id;

    @NotEmpty
    public String userName;

    public boolean active = true;

    public UserName name;

    // work-around for https://github.com/swagger-api/swagger-core/issues/2789
    @Schema(name = USER_EXTENSION_SCHEMA_ID)
    @JsonbProperty(value = USER_EXTENSION_SCHEMA_ID)
    public UserExtension extension;
    public List<UserEmail> emails;
    public List<UserPhone> phoneNumbers;
    public List<String> roles;
    public List<String> groups;

    public Meta meta;

    public UserResultItem() {
        meta = new Meta();
        meta.resourceType = Meta.ResourceTypeEnum.User;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResultItem that = (UserResultItem) o;
        return active == that.active
                && Objects.equals(id, that.id)
                && Objects.equals(userName, that.userName)
                && Objects.equals(name, that.name)
                && Objects.equals(emails, that.emails)
                && Objects.equals(phoneNumbers, that.phoneNumbers)
                && Objects.equals(roles, that.roles)
                && Objects.equals(extension, that.extension)
                && Objects.equals(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName);
    }
}
