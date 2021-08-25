package de.lray.service.admin.user.operation;

import jakarta.validation.ValidationException;

import java.util.Map;

public class InvalidPatchOpValueException extends ValidationException {
    public InvalidPatchOpValueException(Map.Entry<UserPatchOpField, Object> opValue) {
        super(
                String.format(
                        "Patch-Operation value %s is invalid for field %s",
                        opValue.getValue().toString(),
                        opValue.getKey()
                )
        );
    }
}
