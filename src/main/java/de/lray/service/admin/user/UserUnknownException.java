package de.lray.service.admin.user;

public class UserUnknownException extends IllegalArgumentException {
    public UserUnknownException(String message) {
        super(message);
    }
}
