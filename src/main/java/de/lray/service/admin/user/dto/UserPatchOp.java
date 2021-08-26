package de.lray.service.admin.user.dto;

import de.lray.service.admin.user.operation.UserPatchOpAction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@SuppressWarnings("java:S1104")
public class UserPatchOp {
  @NotNull
  @NotEmpty
  public UserPatchOpAction op;
  @NotNull
  public UserPatchOpValues value;
}
