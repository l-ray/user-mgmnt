package de.lray.service.admin.user.dto;

import jakarta.validation.constraints.NotEmpty;

@SuppressWarnings("java:S1104")
public class UserName {
  public String middleName;
  public String givenName;
  @NotEmpty
  public String familyName;
}
