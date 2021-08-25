package de.lray.service.admin.user.dto;

import de.lray.service.admin.user.operation.UserPatchOpAction;
import de.lray.service.admin.user.operation.UserPatchOpField;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@SuppressWarnings("java:S1104")
public class UserPatchOp {
  @NotNull
  @NotEmpty
  public UserPatchOpAction op;
  @NotNull
  public Map<UserPatchOpField, Object> value  = Map.of();
}
