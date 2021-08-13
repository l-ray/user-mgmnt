package de.lray.service.admin.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("java:S1104")
public class Meta {

  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  public String lastModified = null;

//  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  public String created = null;

  public ResourceTypeEnum resourceType = ResourceTypeEnum.User;

  @SuppressWarnings("java:S115")
  public enum ResourceTypeEnum {
    User, Group
  }
}


