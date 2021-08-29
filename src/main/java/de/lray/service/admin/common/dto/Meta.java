package de.lray.service.admin.common.dto;

import java.util.Objects;

@SuppressWarnings("java:S1104")
public class Meta {

  public String lastModified = null;

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


