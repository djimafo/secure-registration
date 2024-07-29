package com.djmcode.registration.controller;

import com.djmcode.registration.service.auth.AuthentificationService;
import com.djmcode.registration.service.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
@Tag(name = "authentification")
public class Authentification
{
  private final AuthentificationService authService ;

  @PostMapping("register")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity <?> register(@RequestBody @Valid RegisterRequest request) throws MessagingException
  {
    authService.registration(request);
    return ResponseEntity.accepted().build();
  }
}
