package com.djmcode.registration.service.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthRequest
{
  @NotEmpty(message = "The e-mail address is required")
  @NotBlank(message = "The e-mail address is required")
  @Email(message = "Incorrect email format")
  private String email;

  @NotEmpty(message = "The Password is required")
  @NotBlank(message = "The Password is required")
  @Size(min = 4 , message = "Incorrect password format")
  private String password;
}
