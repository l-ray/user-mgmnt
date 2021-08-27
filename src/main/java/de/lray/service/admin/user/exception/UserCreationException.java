package de.lray.service.admin.user.exception;

public class UserCreationException extends IllegalArgumentException {
  public UserCreationException(String message) {
    super(message);
  }
}
