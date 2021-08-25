package de.lray.service.admin.user.operation;

import de.lray.service.admin.user.dto.UserPatchOp;
import de.lray.service.admin.user.persistence.entities.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class UserPatchFactory {

  public void apply(User user, List<UserPatchOp> ops) {
    for (UserPatchOp op : ops) {
      Objects.requireNonNull(op.op, "Operation can nott be null");
      switch (op.op) {
        case replace: new ValueReplaceOp().apply(user.getCredentials(), op);
          break;
        default: throw new IllegalArgumentException("Unknown operation ${op.op}");
      }
    }
  }

}
