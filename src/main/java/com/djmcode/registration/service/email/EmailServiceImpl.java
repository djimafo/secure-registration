package com.djmcode.registration.service.email;

import java.util.HashMap;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService
{
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  @Override
  @Async
  public void sendConfirmationMail(
          String toEmail,
          String firstName,
          String subject,
          EmailTemplateName emailTemplateName,
          String urlConfirmation,
          String activationCode
                                  ) throws MessagingException
  {
    String templateName;
    if (templateEngine == null)
    {
      templateName = "confirm-email";
    }
    else
    {
      templateName = emailTemplateName.getName();
    }

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(
            mimeMessage,
            MULTIPART_MODE_MIXED,
            UTF_8.name());

    Map<String, Object> properties = new HashMap<>();
    properties.put("firstName", firstName);
    properties.put("urlConfirmation", urlConfirmation);
    properties.put("activationCode", activationCode);

    Context context = new Context();
    context.setVariables(properties);

    helper.setFrom("jerrymalcaire@yahoo.fr");
    helper.setTo(toEmail);
    helper.setSubject(subject);

    helper.setText(templateEngine.process(templateName, context), true);
    mailSender.send(mimeMessage);

  }
}
