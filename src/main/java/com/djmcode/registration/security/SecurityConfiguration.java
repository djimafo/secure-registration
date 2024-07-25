package com.djmcode.registration.security;

import com.djmcode.registration.service.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)

public class SecurityConfiguration
{
  private final JwtAuthFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
  {
    http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req.requestMatchers(
                                                     "/ath/**",
                                                     "/v2/api-docs",
                                                     "/v3/api-docs",
                                                     "/v3/api-docs/**",
                                                     "/swagger-resources",
                                                     "/swagger-resources/**",
                                                     "/configuration/ui",
                                                     "/configuration/security",
                                                     "/swagger-ui/**",
                                                     "/webjars/**",
                                                     "/swagger-ui.html"
                                                             ).permitAll()
                                             .anyRequest()
                                             .authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter,
                             UsernamePasswordAuthenticationFilter.class); //juste pour placer le filtre jwtAuthFilter avant les filtre de sprigboot et avant celuis de UsernamePasswordAuthenticationFilter.class
    return http.build();
  }

}
