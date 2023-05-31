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
    // 쿠키에서 Access Token 을 가져옴
    String aToken = jwtTokenProvider.resolveToken(request.getCookies(), Token.ACCESS_TOKEN);
    log.info("doFilterInternal - aToken: {}", aToken);
    // 쿠키에서 Refresh Token 을 가져옴
    String rToken = jwtTokenProvider.resolveToken(request.getCookies(), Token.REFRESH_TOKEN);
    log.info("doFilterInternal - rToken: {}", rToken);
    // Access Token 은 없고 Refresh Token 만 있는 경우 로그아웃으로 토큰 제거 처리
    if (aToken == null && rToken != null) {
      forwardToLogout(request, response);
      return;
    }

    try {
      // 토큰이 있으면 유효성 검사 (유효하면 true, 비유효하거나 만료됐다면 CustomException)
      if (aToken != null && jwtTokenProvider.validateToken(aToken)) {
        log.info("doFilterInternal - Valid Access Token");
        // 인증 정보를 가져와 시큐리티 컨택스트에 등록
        Authentication auth = jwtTokenProvider.getAuthentication(aToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (CustomException ex) {
      log.info("doFilterInternal - CustomException: {}", ex.getMessage());
      // 예외가 토큰 만료에 의한 예외라면 refresh 토큰이 유효한지 확인하고 토큰 재발급으로 이동
      if (ex.getMessage().equals("Expired JWT token") && validationRefreshToken(rToken)) {
        forwardToRefreshToken(request, response);
        return;
      }
      // 시큐리티 컨택스트 인증 모두 제거 (This is very important, since it guarantees the user is not authenticated at all)
      SecurityContextHolder.clearContext();
      // 클라이언트에 오류 응답
//        response.sendError(ex.getHttpStatus().value(), ex.getMessage());
      // 로그아웃으로 토큰 제거 처리 - (access 토큰 비유효), (access 토큰이 만료, refresh 토큰 없거나 만료)
      forwardToLogout(request, response);
      // 필터 체인 종료
      return;
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

  private void forwardToLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 로그아웃 처리 및 토큰 제거
    request.getRequestDispatcher("/member/signout").forward(request, response);
  }

  private static void forwardToRefreshToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 토큰 재발급 후 해당 uri로 이동하기 위해 저장
    request.setAttribute("uri", request.getRequestURI());
    // access 토큰을 재발급 받기 위해 포워딩 - (access 토큰이 만료, refresh 토큰 유효)
    request.getRequestDispatcher("/member/refresh").forward(request, response);
  }
}
