package de.lray.service.admin.user.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

@SuppressWarnings("java:S1104")
public class UserPhone {
  @NotEmpty
  public String value;
  public String type;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserPhone userPhone = (UserPhone) o;
    return Objects.equals(value, userPhone.value) && Objects.equals(type, userPhone.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, type);
  }
}
