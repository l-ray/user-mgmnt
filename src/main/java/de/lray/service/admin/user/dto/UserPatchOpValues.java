package de.lray.service.admin.user.dto;

import java.util.StringJoiner;

@SuppressWarnings("java:S1104")
public class UserPatchOpValues {
    public Boolean active;
    public String password;

    @Override
    public String toString() {
        return new StringJoiner(", ", UserPatchOpValues.class.getSimpleName() + "[", "]")
                .add("active=" + active)
                .add("password='" + password + "'")
                .toString();
    }
}
