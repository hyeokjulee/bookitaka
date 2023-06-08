package com.bookitaka.NodeulProject.member.security;

import com.bookitaka.NodeulProject.member.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
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

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("doFilterInternal - getRequestURI : {}", request.getRequestURI());
    // 쿠키에서 Token 을 가져옴
    String aToken = jwtTokenProvider.resolveToken(request.getCookies(), Token.ACCESS_TOKEN);
    String rToken = jwtTokenProvider.resolveToken(request.getCookies(), Token.REFRESH_TOKEN);
    String signoutUri = "/member/signout";
    String refreshUri = "/member/refresh/token";

    // 둘 다 있는 경우
    if (aToken != null && rToken != null) {
      boolean validRToken = validationRefreshToken(rToken);
      try {
        jwtTokenProvider.validateToken(aToken);
        // a토큰이 유효하고 r토큰이 유효하지 않은 경우
        if (!validRToken) {
          // TODO : a토큰이 유효하고, r토큰이 유효하지 않은 경우
        // 둘 다 유효한 경우
        } else {
          // 권한 부여
          Authentication auth = jwtTokenProvider.getAuthentication(aToken);
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (CustomException ex) {
        // a토큰이 만료되었고 r토큰은 유효한 경우
        if (ex.getMessage().equals("Expired JWT token") && validRToken) {
          // 토큰 재발급
          if (!request.getRequestURI().equals(refreshUri)) {
            response.sendRedirect(refreshUri);
            return;
          }
        // a토큰이 유효하지 않고 r토큰은 유효한 경우 || 둘 다 유효하지 않은 경우
        } else {
          // 로그아웃
          if (!request.getRequestURI().equals(signoutUri)) {
            response.sendRedirect(signoutUri);
            return;
          }
        }
      // 시큐리티 컨택스트 인증 모두 제거 (This is very important, since it guarantees the user is not authenticated at all)
      SecurityContextHolder.clearContext();
      }
    // 둘 다 없는 경우
    } else if (aToken == null && rToken == null) {
      // 권한 없음
      // 로그아웃을 해서 토큰이 삭제되고 리다이렉트 시에 여기를 거침
    // a토큰이 없고 r토큰은 있는 경우 || r토큰이 없고 a토큰은 있는 경우
    } else {
      // 로그아웃
      if (!request.getRequestURI().equals(signoutUri)) {
        response.sendRedirect(signoutUri);
        return;
      }
    }
    filterChain.doFilter(request, response);
  }


  public boolean validationRefreshToken(String rToken) {
    try {
      // 토큰이 있으면 유효성 검사 (유효하면 true, 비유효하거나 만료됐다면 CustomException)
      if (rToken != null && jwtTokenProvider.validateToken(rToken)) {
        log.info("doFilterInternal - validationRefreshToken - valid Token");
        return true;
      }
    } catch (CustomException ex) {
      log.info("doFilterInternal - validationRefreshToken - CustomException: {}", ex.getMessage());
    }
    return false;
  }

}
