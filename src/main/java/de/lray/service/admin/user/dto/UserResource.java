package de.lray.service.admin.user.dto;

import java.util.Objects;
import java.util.StringJoiner;

@SuppressWarnings("java:S1104")
public class  UserResource extends UserResultItem {

  public UserResource() {
    super();
  }

  public String displayName;
  public String preferredLanguage;
  public String locale;
  public String timezone;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    UserResource that = (UserResource) o;
    return Objects.equals(displayName, that.displayName)
            && Objects.equals(preferredLanguage, that.preferredLanguage)
            && Objects.equals(locale, that.locale)
            && Objects.equals(timezone, that.timezone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), displayName, preferredLanguage, locale, timezone);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ")
            .add("displayName='" + displayName + "'")
            .add("id='" + id + "'")
            .add("userName='" + userName + "'")
            .toString();
  }
}