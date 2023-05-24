package com.bookitaka.NodeulProject.member.security;

import com.bookitaka.NodeulProject.member.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    // Retrieve access token from HTTP-only cookie
    Cookie[] cookies = httpServletRequest.getCookies();
    String accessToken = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("access_token")) {
          accessToken = cookie.getValue();
          break;
        }
      }
    }

    try {
      if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
        Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (CustomException ex) {
      // This is very important, since it guarantees the user is not authenticated at all
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
      return;
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }


//  @Override
//  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//    String token = jwtTokenProvider.resolveToken(httpServletRequest);
//    try {
//      if (token != null && jwtTokenProvider.validateToken(token)) {
//        Authentication auth = jwtTokenProvider.getAuthentication(token);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//      }
//    } catch (CustomException ex) {
//      //this is very important, since it guarantees the user is not authenticated at all
//      SecurityContextHolder.clearContext();
//      httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
//      return;
//    }
//
//    filterChain.doFilter(httpServletRequest, httpServletResponse);
//  }

}
