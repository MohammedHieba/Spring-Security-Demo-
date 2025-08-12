package com.spring.security.demo.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;
}