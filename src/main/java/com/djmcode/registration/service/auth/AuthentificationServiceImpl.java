package com.djmcode.registration.service.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import com.djmcode.registration.entitie.Token;
import com.djmcode.registration.entitie.User;
import com.djmcode.registration.repo.RoleRepository;
import com.djmcode.registration.repo.TokenRepository;
import com.djmcode.registration.repo.UserRepository;
import com.djmcode.registration.service.email.EmailService;
import com.djmcode.registration.service.email.EmailTemplateName;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthentificationServiceImpl implements AuthentificationService
{
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final EmailService emailService;

  @Value("${application.mailding.activation_url}")
  private String activationUrl;

  @Override
  public void registration(RegisterRequest request) throws MessagingException
  {
    var role = roleRepository.findByName("USER").orElseThrow(() -> new IllegalStateException("Role not found in Table Role"));
    User user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .birthday(request.getBirthday())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(List.of(role))
                    .enabled(false)
                    .build();
    userRepository.save(user);

    sendActivationEmail(user);
  }

  private void sendActivationEmail(User user) throws MessagingException
  {
    var activationtCode = genrateActivationCode(6);
    Token token = Token.builder()
                       .user(user)
                       .createTime(LocalDateTime.now())
                       .expireTime(LocalDateTime.now().plusMinutes(15))
                       .token(activationtCode)
                       .build();

    tokenRepository.save(token);

    String email = user.getEmail();
    String firstname = user.getFirstname();
    emailService.sendConfirmationMail(email, firstname, "Account Activation", EmailTemplateName.ACTIVATE_ACCOUNT, activationUrl, activationtCode);

  }

  private String genrateActivationCode(int length)
  {
    String characters = "0123456789";
    StringBuilder activationCode = new StringBuilder();
    SecureRandom secureRandom = new SecureRandom();
    for (int i = 0; i < length; i++)
    {
      int index = secureRandom.nextInt(characters.length()); // index between 0 and 9
      activationCode.append(characters.charAt(index)); // append the character corresponding to the index
    }
    return activationCode.toString();
  }


}
