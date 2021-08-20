package de.lray.service.admin.user.dto;

import jakarta.validation.constraints.Email;

@SuppressWarnings("java:S1104")
public class UserEmail {
  @Email
  public String value;
  public boolean primary;
  public String type;
  public String display;
}
