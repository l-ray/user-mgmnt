package de.lray.service.admin.user.dto;

import de.lray.service.admin.user.persistence.patch.UserPatchOpAction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.StringJoiner;

@SuppressWarnings("java:S1104")
public class UserPatchOp {
  @NotNull
  @NotEmpty
  public UserPatchOpAction op;
  @NotNull
  public UserPatchOpValues value;

  @Override
  public String toString() {
    return new StringJoiner(", ", UserPatchOp.class.getSimpleName() + "[", "]")
            .add("op=" + op)
            .add("value=" + value)
            .toString();
  }
}
