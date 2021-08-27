package de.lray.service.admin.user.exception;

public class UserUnknownException extends IllegalArgumentException {
    public UserUnknownException(String message) {
        super(message);
    }
}
