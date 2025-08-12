package com.spring.security.demo.controller.auth;


import com.spring.security.demo.dto.JWTResponseDto;
import com.spring.security.demo.dto.LoginRequestDto;
import com.spring.security.demo.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
