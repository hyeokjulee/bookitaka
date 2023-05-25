package com.bookitaka.NodeulProject.member.security;

import com.bookitaka.NodeulProject.member.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Value("${access.token.cookie.name}")
  private String accessTokenCookieName;
  @Value("${refresh.token.cookie.name}")
  private String refreshTokenCookieName;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    // Retrieve access token from HTTP-only cookie
    String token = jwtTokenProvider.resolveToken(httpServletRequest, accessTokenCookieName);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (CustomException ex) {
      if (ex.getMessage().equals("Expired JWT token")) {
        log.info("Expired JWT token");
        String rToken = jwtTokenProvider.resolveToken(httpServletRequest, refreshTokenCookieName);
        if (rToken != null && jwtTokenProvider.validateToken(rToken)) {
          httpServletResponse.sendRedirect("/member/refresh");
          return;
        }
      }
      // This is very important, since it guarantees the user is not authenticated at all
      SecurityContextHolder.clearContext();
//      httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
      log.info("httpServletRequest.getRequestURI : {}", httpServletRequest.getRequestURI());
      if (httpServletRequest.getRequestURI().equals("/member/signin")
              || httpServletRequest.getRequestURI().equals("/members/login")
              || httpServletRequest.getRequestURI().equals("/member/token")) {
      } else {
        httpServletResponse.sendRedirect("/members/login");
        return;
      }
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
