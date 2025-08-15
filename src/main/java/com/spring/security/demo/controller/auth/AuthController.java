package com.spring.security.demo.controller.auth;


import com.spring.security.demo.dto.AccessTokenDto;
import com.spring.security.demo.dto.JWTResponseDto;
import com.spring.security.demo.dto.LoginRequestDto;
import com.spring.security.demo.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<JWTResponseDto> login (@RequestBody LoginRequestDto loginRequest){

        JWTResponseDto jwtResponseDto = authService.login(loginRequest.getLogin(), loginRequest.getPassword());

        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenDto> refreshAccessToken(HttpServletRequest request) {
        final String jwtTokenHeader= request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = jwtTokenHeader.substring("Bearer ".length());
        AccessTokenDto dto = authService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        final String jwtTokenHeader= request.getHeader(HttpHeaders.AUTHORIZATION);
        String accessToken = jwtTokenHeader.substring("Bearer ".length());
        authService.logoutUser(accessToken);

        return ResponseEntity.ok(null);
    }

}
