package de.lray.service.admin.user.dto;

import de.lray.service.admin.common.Meta;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("java:S1104")
public class UserResultItem {

    public static final List<String> schemas = Arrays.asList("urn:ietf:params:scim:schemas:core:2.0:User");

    public String id;

    @NotEmpty
    public String userName;

    public boolean active = true;

    public UserName name;
    public List<UserEmail> emails;
    public List<UserPhone> phoneNumbers;
    public List<String> roles;

    public Meta meta;

    public UserResultItem() {
        meta = new Meta();
        meta.resourceType = Meta.ResourceTypeEnum.User;
    }
}
