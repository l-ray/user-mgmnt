package de.lray.service.admin.user.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.StringJoiner;

@SuppressWarnings("java:S1104")
public class UserAdd extends UserResource {

  @NotEmpty
  protected String password = null;

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", UserAdd.class.getSimpleName() + "[", "]")
            .add("password='" + (password == null ? "is null" : password.substring(0,1)+"***") + "'")
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
