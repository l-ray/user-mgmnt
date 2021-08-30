package de.lray.service.admin.user.persistence.patch;

import de.lray.service.admin.user.dto.UserPatchOp;
import de.lray.service.admin.user.persistence.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class UserPatchFactory {

  private final List<ValuePatcher> ops;

  public UserPatchFactory() {
    this.ops = List.of(
            new ValueReplacePatcher()
    );
  }

  public void apply(User user, List<UserPatchOp> ops) {
    for (UserPatchOp op : ops) {
      Objects.requireNonNull(op.op, "Operation can not be null");
      var opPatcher = this.ops.stream()
              .filter((ValuePatcher vpo) -> vpo.canApply(op))
              .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown operation ${op.op}"));
      opPatcher.apply(user.getCredentials(), op);
    }
  }

}
