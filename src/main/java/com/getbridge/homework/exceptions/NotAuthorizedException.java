package com.getbridge.homework.exceptions;

public class NotAuthorizedException extends RuntimeException {

  public NotAuthorizedException() {
    super(
        "The user provided in the X_AUTHENTICATED_USER is not allowed to execute this operation.");
  }
}
