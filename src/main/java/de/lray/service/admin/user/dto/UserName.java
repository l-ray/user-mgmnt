package de.lray.service.admin.user.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

@SuppressWarnings("java:S1104")
public class UserName {
  public String middleName;
  public String givenName;
  @NotEmpty
  public String familyName;

  public UserName() {}

  public UserName(String middleName, String givenName, String familyName) {
    this.middleName = middleName;
    this.givenName = givenName;
    this.familyName = familyName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserName userName = (UserName) o;
    return Objects.equals(middleName, userName.middleName)
            && Objects.equals(givenName, userName.givenName) && Objects.equals(familyName, userName.familyName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(middleName, givenName, familyName);
  }
}
