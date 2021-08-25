package de.lray.service.admin.user.operation;

import de.lray.service.admin.user.dto.UserPatchOp;
import de.lray.service.admin.user.persistence.entities.Credentials;

import java.util.Map;

public class ValueReplaceOp {

  public static final String KEY = "replace";

  void apply(Credentials credential, UserPatchOp patchOp) {
    for (Map.Entry<UserPatchOpField, Object> opValue : patchOp.value.entrySet()) {
      try {
        switch (opValue.getKey()) {
          case active:
            credential.setActive((Boolean) opValue.getValue());
            break;
          case password:
            // credential.Password = ScimUtil.hashPasswordString(opValue.Value as String)
            break;
          default:
            throw new IllegalArgumentException("Unknown opValue-Key ${opValue.Key} "
                    + "with value ${opValue.Value}");
        }
      } catch (ClassCastException ex) {
            throw new InvalidPatchOpValueException(opValue);
        }
    }
  }

}
