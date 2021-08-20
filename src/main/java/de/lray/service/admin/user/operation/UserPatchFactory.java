package de.lray.service.admin.user.operation;

import de.lray.service.admin.user.dto.UserPatchOp;

import java.util.List;

public class UserPatchFactory {

  void apply(List<UserPatchOp> ops) {
    for (UserPatchOp op : ops) {
      switch (op.op) {
        case ValueReplaceOp.KEY: new ValueReplaceOp().apply(op);
          break;
        default: throw new IllegalArgumentException("Unknown operation ${op.op}");
      }
    }
  }

}
