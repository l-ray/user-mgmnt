package de.lray.service.admin.user.operation;

import de.lray.service.admin.user.dto.UserPatchOp;

import java.util.Map;

public class ValueReplaceOp {

  public static final String KEY = "replace";


  void apply(UserPatchOp patchOp) {
    for (Map.Entry opValue : patchOp.value.entrySet()) {
      switch ((UserPatchOpField) opValue.getKey() ) {
        case active:
          //credential.Active = opValue.Value as Boolean
          break;
        case password:
          // credential.Password = ScimUtil.hashPasswordString(opValue.Value as String)
          break;
        default:
          throw new IllegalArgumentException("Unknown opValue-Key ${opValue.Key} "
              + "with value ${opValue.Value}");
      }
    }
  }

}
