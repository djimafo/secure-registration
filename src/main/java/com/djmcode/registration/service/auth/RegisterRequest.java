package com.djmcode.registration.service.auth;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RegisterRequest
{
  @NotEmpty(message = "")
  @NotBlank(message = "")
  private String firstname;

  @NotEmpty(message = "")
  @NotBlank(message = "")
  private String lastname;

  //@NotEmpty(message = "")
  //@NotBlank(message = "")
  private Date birthday;

  @NotEmpty(message = "")
  @NotBlank(message = "")
  @Email(message = "")
  private String email;

  @NotEmpty(message = "")
  @NotBlank(message = "")
  @Size(min = 4 , message = "")
  private String password;
}
