package de.lray.service.admin.common;

import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("java:S1104")
public class Error {

  public static final List<String> schemas
          = Arrays.asList("urn:ietf:params:scim:api:messages:2.0:Error");
  public int status = HttpStatus.SC_NOT_FOUND;
  public String detail = "Not Found";

  public static Error notFound(String detail) {
    detail = Objects.requireNonNullElse(detail, "404 - Found");
    return new Error(HttpStatus.SC_NOT_FOUND, detail);
  }

  public static Error alreadyExists(String detail) {
    detail = Objects.requireNonNullElse(detail, "409 - Already existing");
    return new Error(HttpStatus.SC_CONFLICT, detail);
  }

  public static Error invalidArgument(String detail) {
    detail = Objects.requireNonNullElse(detail, "400 - Bad Request");
    return new Error(HttpStatus.SC_BAD_REQUEST, detail);
  }

  public Error() {}

  private Error( int statusParam, String detailParam) {
    this.status = statusParam;
    this.detail = detailParam;
  }
}
