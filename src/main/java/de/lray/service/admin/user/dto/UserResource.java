package de.lray.service.admin.user.dto;

@SuppressWarnings("java:S1104")
public class  UserResource extends UserResultItem {

  public UserResource() {
    super();
  }

  public String displayName;
  public String preferredLanguage;
  public String locale;
  public String timezone;
}