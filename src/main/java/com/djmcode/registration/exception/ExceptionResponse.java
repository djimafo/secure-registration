package com.djmcode.registration.exception;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class ExceptionResponse
{

  private Integer errorCode;
  private String descriptionError;
  private String error;
  private Set<String> validationErrors;
  private Map<String, String> errors;
}
