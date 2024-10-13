package com.djmcode.registration.service.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName
{
  ACTIVATE_ACCOUNT("activate_account")
  ; // template name

  private final String name;

  EmailTemplateName(String name)
  {
    this.name = name;
  }
}
