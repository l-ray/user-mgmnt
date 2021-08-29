package de.lray.service.admin.user.dto;

import java.util.StringJoiner;

@SuppressWarnings("java:S1104")
public class UserPatchOpValues {
    //@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public Boolean active;
    //@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public String password;

    @Override
    public String toString() {
        return new StringJoiner(", ", UserPatchOpValues.class.getSimpleName() + "[", "]")
                .add("active=" + active)
                .add("password='" + password + "'")
                .toString();
    }
}
