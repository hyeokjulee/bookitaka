package com.bookitaka.NodeulProject.member.security;

import com.bookitaka.NodeulProject.member.exception.CustomException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  /**
   * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
   * microservices environment, this key would be kept on a config-server.
   */
  private final MyUserDetails myUserDetails;

  @Value("${security.jwt.token.secret-key:secret-key}")
  private String secretKey;

  @Value("${security.jwt.token.access.expire-length:3600000}")
  private long access_validityInMilliseconds = 3600000; // 1h

  @Value("${security.jwt.token.refresh.expire-length:1209600000}")
  private long refresh_validityInMilliseconds = 1209600000; // 14d

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  /************************************************************************************************
   * Create Token
   ************************************************************************************************/

  public String createToken(String memberEmail, String memberRole) {
    return _buildToken(memberEmail, memberRole, access_validityInMilliseconds);
  }

  public String createRefreshToken(String memberEmail, String memberRole) {
    return _buildToken(memberEmail, memberRole, refresh_validityInMilliseconds);
  }

  private String _buildToken(String memberEmail, String memberRole, long expTime) {
    Claims claims = Jwts.claims().setSubject(memberEmail);
//    claims.put("auth", appUserRoles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));
    claims.put("auth", memberRole);

    Date now = new Date();
    Date validity = new Date(now.getTime() + expTime);

    return Jwts.builder()//
            .setClaims(claims)//
            .setIssuedAt(now)//
            .setExpiration(validity)//
            .signWith(SignatureAlgorithm.HS256, secretKey)//
            .compact();
  }

  /************************************************************************************************
   * Find Token
   ************************************************************************************************/

  // 쿠키 목록에서 토큰을 찾아 반환
  public String resolveToken(Cookie[] cookies, String tokenCookieName) {
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(tokenCookieName)) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  /************************************************************************************************
   * Get * From Token
   ************************************************************************************************/

  // 토큰에서 권한 정보 얻기
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(getMemberEmail(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // 토큰에서 이메일 얻기
  public String getMemberEmail(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
  }

  // 토큰에서 만료 시간 얻기
  public Date getExpirationDate(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration();
  }

  // 토큰의 유효성을 검사
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new CustomException("Expired JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomException("Invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
