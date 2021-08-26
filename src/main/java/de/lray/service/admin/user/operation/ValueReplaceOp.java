package de.lray.service.admin.user.operation;

import de.lray.service.admin.user.dto.UserPatchOp;
import de.lray.service.admin.user.persistence.entities.Credentials;

public class ValueReplaceOp {

  public static final String KEY = "replace";

  void apply(Credentials credential, UserPatchOp patchOp) {
    var patchOpVal = patchOp.value;

    if (patchOpVal.active != null) {
      credential.setActive(patchOpVal.active);
    }

    if (patchOpVal.password != null) {
      // credential.Password = ScimUtil.hashPasswordString(opValue.Value as String)
    }
  }
}
