package com.djmcode.registration.service.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter
{
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
          @NotNull HttpServletRequest request,
          @NotNull HttpServletResponse response,
          @NotNull FilterChain filterChain
                                 ) throws ServletException, IOException
  {
    if (request.getServletPath().contains("/api/v1/auth"))
    {
      filterChain.doFilter(request, response);
      return;
    }
    final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String jwt;
    final String email;

    if (authorization == null || !authorization.startsWith("Bearer "))
    {

      filterChain.doFilter(request, response);
      return;
    }

    jwt = authorization.substring(7);
    email = jwtService.extractEmail(jwt);

    //evaluates whether the user exists and is already logged in
    if (email == null && SecurityContextHolder.getContext().getAuthentication() == null)
    {
      UserDetails user = this.userDetailsService.loadUserByUsername(email);
      if (jwtService.isTokenValid(jwt, user))
      {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}