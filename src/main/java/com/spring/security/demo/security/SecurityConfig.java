package com.spring.security.demo.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
public class SecurityConfig {

    String [] PUBLIC_END_POINTS = {"/api/v1/auth/login"};



    // inject authenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }




    // inject webSecurityFilter bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider customAuthenticationProvider,
                                                   OncePerRequestFilter jwtAuthFilter, AuthenticationEntryPoint jwtUnAuthResponse) throws Exception {

            http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                    .httpBasic(auth-> auth.authenticationEntryPoint(jwtUnAuthResponse))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtUnAuthResponse))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_END_POINTS).permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement(session
                            -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(customAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
