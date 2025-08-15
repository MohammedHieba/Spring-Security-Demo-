package com.spring.security.demo.security.utils;

import com.spring.security.demo.dto.AppUserDetail;
import com.spring.security.demo.model.TokenInfo;
import com.spring.security.demo.service.auth.TokenInfoService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Log4j2
@Component
public class JwtTokenUtils {

    private static String TOKEN_SECRET;
    private static Long ACCESS_TOKEN_VALIDITY;
    private static Long REFRESH_TOKEN_VALIDITY;
    private static SecretKey secretKey;

    @Autowired
    private TokenInfoService  tokenInfoService;

    public JwtTokenUtils(@Value("${auth.secret}") String secret, @Value("${auth.access.expiration}") Long accessValidity
            , @Value("${auth.refresh.expiration}") Long refreshValidity) {
        Assert.notNull(accessValidity, "Validity must not be null");
        Assert.hasText(secret, "Validity must not be null or empty");

        secretKey =  Keys.hmacShaKeyFor(secret.getBytes());;
        ACCESS_TOKEN_VALIDITY = accessValidity;
        REFRESH_TOKEN_VALIDITY = refreshValidity;
    }

    public static String generateToken(final String username, final String tokenId , boolean isRefresh) {

        return Jwts.builder()
                .setId(tokenId)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setIssuer("app-Service")
                .setExpiration(calcTokenExpirationDate(isRefresh))
                .claim("created", Calendar.getInstance().getTime())
                .signWith(secretKey).compact();
    }


    private static Date calcTokenExpirationDate(boolean isRefresh) {
        return new Date(System.currentTimeMillis() + (isRefresh ? REFRESH_TOKEN_VALIDITY : ACCESS_TOKEN_VALIDITY) * 1000);
    }

    public String getUserNameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public String getTokenIdFromToken(String token) {
        return getClaims(token).getId();
    }

    public boolean isTokenValid(String token, AppUserDetail userDetails) {
        log.info("isTokenExpired >>> " + isTokenExpired(token));
//        String username = getUserNameFromToken(token);
//        log.info("username from token >>> " + username);
//        log.info("userDetails.getUsername >>> " + userDetails.getUsername());
//        log.info("username =  >>> userDetails.getUsername >>> " + username.equals(userDetails.getUsername()));
//        Boolean isUserNameEqual = username.equalsIgnoreCase(userDetails.getUsername());

        return (!isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token , HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {

        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
             isTokenNotRevoked(token);
            return true;
        }catch (RuntimeException ex){

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
            return false;
        }
    }

    private void isTokenNotRevoked(String accessToken) {
         Optional<TokenInfo> token = tokenInfoService.findByAccessToken(accessToken);
         if(!token.isPresent()) throw new RuntimeException("Token is not valid");
    }

}
