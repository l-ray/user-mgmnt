package de.lray.service.admin.user.dto;

import jakarta.validation.constraints.Email;

import java.util.Objects;

@SuppressWarnings("java:S1104")
public class UserEmail {
  @Email
  public String value;
  public boolean primary;
  public String type;
  public String display;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserEmail userEmail = (UserEmail) o;
    return primary == userEmail.primary && Objects.equals(value, userEmail.value) && Objects.equals(type, userEmail.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, primary, type);
  }
}
