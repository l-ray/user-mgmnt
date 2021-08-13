package de.lray.service.admin.user.dto;

import jakarta.validation.constraints.NotEmpty;

@SuppressWarnings("java:S1104")
public class UserPhone {
  @NotEmpty
  public String value;
  public String type;
}
