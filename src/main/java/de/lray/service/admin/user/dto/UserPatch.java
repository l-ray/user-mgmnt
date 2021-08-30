package de.lray.service.admin.user.dto;


import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@SuppressWarnings({"java:S1104","java:S116"})
public class UserPatch {
  @NotNull
  public List<String> schemas = List.of("urn:ietf:params:scim:api:messages:2.0:PatchOp" );
  public List<UserPatchOp> Operations;

  @Override
  public String toString() {
    return new StringJoiner(", ", UserPatch.class.getSimpleName() + "[", "]")
            .add("Operations=" + Operations)
            .toString();
  }
}
