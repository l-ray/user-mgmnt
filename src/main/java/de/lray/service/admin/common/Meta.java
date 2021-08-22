package de.lray.service.admin.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@SuppressWarnings("java:S1104")
public class Meta {

  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  public String lastModified = null;

//  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  public String created = null;

  public ResourceTypeEnum resourceType = ResourceTypeEnum.User;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Meta meta = (Meta) o;
    return Objects.equals(lastModified, meta.lastModified) && Objects.equals(created, meta.created) && resourceType == meta.resourceType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastModified, created, resourceType);
  }

  @SuppressWarnings("java:S115")
  public enum ResourceTypeEnum {
    User, Group
  }
}


