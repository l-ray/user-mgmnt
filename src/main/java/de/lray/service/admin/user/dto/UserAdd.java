package de.lray.service.admin.user.dto;

import de.lray.service.admin.user.validation.PasswordConstraints;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.StringJoiner;

@SuppressWarnings("java:S1104")
public class UserAdd extends UserResource {

  @Size(min=8)
  @PasswordConstraints(message = "Password is not compliant.", containsSpecialChar = false)
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    UserAdd userAdd = (UserAdd) o;
    return Objects.equals(password, userAdd.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), password);
  }
}
