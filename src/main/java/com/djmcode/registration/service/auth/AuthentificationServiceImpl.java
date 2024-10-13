package com.djmcode.registration.service.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import com.djmcode.registration.entitie.Token;
import com.djmcode.registration.entitie.User;
import com.djmcode.registration.repo.RoleRepository;
import com.djmcode.registration.repo.TokenRepository;
import com.djmcode.registration.repo.UserRepository;
import com.djmcode.registration.service.email.EmailService;
import com.djmcode.registration.service.email.EmailTemplateName;
import com.djmcode.registration.service.jwt.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

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

  @Override
  public AuthResponse authentication(AuthRequest request)
  {
    var authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    var user = ((User) authenticate.getPrincipal());
    var claims = new HashMap<String, Object>();
    claims.put("fullName", user.getFullName());
    String jwToken = jwtService.generateToken(claims,user);

 return  AuthResponse.builder()
         .token(jwToken).build();
  }

  @Override
  public void activateAccount(String activateToken) throws MessagingException
  {
    Token token =  tokenRepository.findByToken(activateToken).orElseThrow(() -> new IllegalStateException("Token not found in Table Token"));
    if (LocalDateTime.now().isAfter(token.getExpireTime())){
      sendActivationEmail(token.getUser());
      throw new IllegalStateException("Token is expired, A new token has been send to "+ token.getUser().getEmail());
    }

    User user = token.getUser();
    user.setEnabled(true);
    userRepository.save(user);
    token.setValidateAt(LocalDateTime.now());
    tokenRepository.save(token);
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
    activationUrl=activationUrl+"?token="+activationtCode;
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
