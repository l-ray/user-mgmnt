package de.lray.service.admin.user.persistence.patch;

import de.lray.service.admin.user.dto.UserPatchOp;
import de.lray.service.admin.user.persistence.entities.Credentials;

public class ValueReplacePatcher implements ValuePatcher {

    @Override
    public boolean canApply(UserPatchOp op) {
        return op.op == UserPatchOpAction.replace;
    }

    @Override
    public void apply(Credentials credential, UserPatchOp patchOp) {
        var patchOpVal = patchOp.value;

        if (patchOpVal.active != null) {
            credential.setActive(patchOpVal.active);
        }

        if (patchOpVal.password != null) {
            if (credential.checkPassword(patchOpVal.password)) {
                throw new IllegalArgumentException("Can not change password.");
            } else {
                credential.setPassword(patchOpVal.password);
            }
        }
    }
}
