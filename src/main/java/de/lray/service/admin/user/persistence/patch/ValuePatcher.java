package de.lray.service.admin.user.persistence.patch;

import de.lray.service.admin.user.dto.UserPatchOp;
import de.lray.service.admin.user.persistence.entities.Credentials;

public interface ValuePatcher {
    void apply(Credentials credential, UserPatchOp patchOp);
    boolean canApply(UserPatchOp op);
}
