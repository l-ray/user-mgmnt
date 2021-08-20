package de.lray.service.admin.user.dto;


import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("java:S1104")
public class UserPatch {
  @NotNull
  public List<String> schemas = Arrays.asList("urn:ietf:params:scim:api:messages:2.0:PatchOp" );
  public List<UserPatchOp> Operations;
}
