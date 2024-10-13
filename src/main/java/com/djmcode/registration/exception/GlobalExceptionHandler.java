package com.djmcode.registration.exception;

import java.util.HashSet;
import java.util.Set;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.djmcode.registration.exception.ErrorCode.ACCOUNT_EXPIRED;
import static com.djmcode.registration.exception.ErrorCode.ACCOUNT_IS_DISABLED;
import static com.djmcode.registration.exception.ErrorCode.ACCOUNT_LOCKED;
import static com.djmcode.registration.exception.ErrorCode.BAD_CREDENTIALS;

@RestControllerAdvice
public class GlobalExceptionHandler
{
  @ExceptionHandler(LockedException.class)
  public ResponseEntity<ExceptionResponse> handleException(LockedException e)
  {
    //e.printStackTrace();

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder()
                                                                                .error(e.getMessage())
                                                                                .errorCode(ACCOUNT_LOCKED.getCode())
                                                                                .descriptionError(ACCOUNT_LOCKED.getDescription())
                                                                                .build());
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ExceptionResponse> handleException(DisabledException e)
  {
    //e.printStackTrace();

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder()
                                                                                .error(e.getMessage())
                                                                                .errorCode(ACCOUNT_IS_DISABLED.getCode())
                                                                                .descriptionError(ACCOUNT_IS_DISABLED.getDescription())
                                                                                .build());
  }

  @ExceptionHandler(AccountExpiredException.class)
  public ResponseEntity<ExceptionResponse> handleException(AccountExpiredException e)
  {
    //e.printStackTrace();

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder()
                                                                                .error(e.getMessage())
                                                                                .errorCode(ACCOUNT_EXPIRED.getCode())
                                                                                .descriptionError(ACCOUNT_EXPIRED.getDescription())
                                                                                .build());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException e)
  {
    //e.printStackTrace();

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder()
                                                                                .error(e.getMessage())
                                                                                .errorCode(BAD_CREDENTIALS.getCode())
                                                                                .descriptionError(BAD_CREDENTIALS.getDescription())
                                                                                .build());
  }

  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<ExceptionResponse> handleException(MessagingException e)
  {
    //e.printStackTrace();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.builder()
                                                                                         .error(e.getMessage())
                                                                                         .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e)
  {
    // e.printStackTrace();
    Set<String> errors = new HashSet<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      System.out.println(error.toString());
      var errorMessage = error.getDefaultMessage();
      errors.add(errorMessage);
    });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                                                                               .validationErrors(errors)
                                                                               .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleException(Exception e)
  {
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponse.builder()
                                                                                         .error(e.getMessage())
                                                                                         .descriptionError(
                                                                                                 "Internal Error contact the admin")
                                                                                         .build());
  }
}
