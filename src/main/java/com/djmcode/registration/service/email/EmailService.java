package com.djmcode.registration.service.email;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService
{
  void sendConfirmationMail(String toEmail,
                            String firstName,
                            String subject,
                            EmailTemplateName emailTemplateName,
                            String urlConfirmation,
                            String activationCode) throws MessagingException;
}
