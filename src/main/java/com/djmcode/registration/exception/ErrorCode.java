package com.djmcode.registration.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public enum ErrorCode
{
  NO_CODE(0, NOT_IMPLEMENTED, "Not implemented"),
  INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Incorrect current password"),
  NOT_MATCHED_PASSWORD(301, BAD_REQUEST, "Not matched password"),
  ACCOUNT_ALREADY_EXISTS(302, FORBIDDEN, "User Account already exists"),
  ACCOUNT_NOT_EXISTS(303, FORBIDDEN, "User Account not exists"),
  ACCOUNT_LOCKED(304, FORBIDDEN, "User Account locked"),
  ACCOUNT_EXPIRED(305, FORBIDDEN, "User Account expired"),
  ACCOUNT_IS_DISABLED(306, FORBIDDEN, "User Account is disabled"),
  BAD_CREDENTIALS(307, FORBIDDEN, "LOgin and / or password is incorrect"),
  ;
  @Getter
  private final Integer code;
  @Getter
  private final String description;
  @Getter
  private final HttpStatus httpStatus;

  ErrorCode(Integer code, HttpStatus httpStatus, String description)
  {
    this.code = code;
    this.httpStatus = httpStatus;
    this.description = description;
  }
}
