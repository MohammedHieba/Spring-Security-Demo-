package com.spring.security.demo.service.auth;

import com.spring.security.demo.dto.AccessTokenDto;
import com.spring.security.demo.dto.AppUserDetail;
import com.spring.security.demo.dto.JWTResponseDto;
import com.spring.security.demo.model.AppUser;
import com.spring.security.demo.model.TokenInfo;
import com.spring.security.demo.security.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@Transactional
public class AuthService {

    @Autowired
    private AuthenticationProvider customAuthenticationProvider;
    @Autowired
    private HttpServletRequest httpRequest;
    @Autowired
    private TokenInfoService tokenInfoService;
    @Autowired
    private  JwtTokenUtils jwtTokenUtils;

    public JWTResponseDto login(String login, String password) {


        Authentication authentication = customAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(login, password));

        log.debug("Valid userDetails credentials.");

        AppUserDetail userDetails = (AppUserDetail) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("SecurityContextHolder updated. [login={}]", login);


        TokenInfo tokenInfo = this.createLoginToken(login, userDetails.getUser());


        return JWTResponseDto.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
    }


    private TokenInfo createLoginToken(String userName, AppUser loggedUser) {
        String userAgent = httpRequest.getHeader(HttpHeaders.USER_AGENT);
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String accessTokenId = UUID.randomUUID().toString();
        String accessToken = jwtTokenUtils.generateToken(userName, accessTokenId, false);
        log.info("Access token created. [tokenId={}]", accessTokenId);

        String refreshTokenId = UUID.randomUUID().toString();
        String refreshToken = jwtTokenUtils.generateToken(userName, refreshTokenId, true);
        log.info("Refresh token created. [tokenId={}]", accessTokenId);

        TokenInfo tokenInfo = new TokenInfo(accessToken, refreshToken);
        tokenInfo.setUser(loggedUser);
        tokenInfo.setUserAgentText(userAgent);
        tokenInfo.setLocalIpAddress(ip.getHostAddress());
        tokenInfo.setRemoteIpAddress(httpRequest.getRemoteAddr());

        return tokenInfoService.save(tokenInfo);
    }


    public AccessTokenDto refreshAccessToken(String refreshToken) {
        if (jwtTokenUtils.isTokenExpired(refreshToken)) {
            return null;
        }
        String userName = jwtTokenUtils.getUserNameFromToken(refreshToken);
        Optional<TokenInfo> refresh = tokenInfoService.findByRefreshToken(refreshToken);
        if (!refresh.isPresent()) {
            return null;
        }

        return new AccessTokenDto(JwtTokenUtils.generateToken(userName, UUID.randomUUID().toString(), false));

    }


    public void logoutUser(String accessToken) {
        Optional<TokenInfo> tokenInfo = tokenInfoService.findByAccessToken(accessToken);
        if (tokenInfo.isPresent()) {
            tokenInfoService.revokeToken(tokenInfo.get().getId());
        }

    }

}
