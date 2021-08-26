package de.lray.service.admin.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("java:S1104")
public class UserPatchOpValues {
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public Boolean active;
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public String password;
}
