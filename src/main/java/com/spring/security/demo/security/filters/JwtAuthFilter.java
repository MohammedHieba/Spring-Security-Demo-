package com.spring.security.demo.security.filters;

import com.spring.security.demo.dto.AppUserDetail;
import com.spring.security.demo.security.utils.JwtTokenUtils;
import com.spring.security.demo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Log4j2
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userService;
    @Autowired
    private JwtTokenUtils tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        logger.info("Path is >> " + request.getRequestURL());
        final SecurityContext securityContext = SecurityContextHolder.getContext();
       if(jwtTokenHeader != null) {
           if (jwtTokenHeader.contains("Bearer") && securityContext.getAuthentication() == null) {
               String jwtToken = jwtTokenHeader.substring("Bearer ".length());
               if (tokenUtil.validateToken(jwtToken, request)) {
                   String username = this.tokenUtil.getUserNameFromToken(jwtToken);
                   if (username != null) {
                       AppUserDetail userDetails = (AppUserDetail) this.userService.loadUserByUsername(username);
                       if (tokenUtil.isTokenValid(jwtToken, userDetails)) {
                           UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                   userDetails, null, userDetails.getAuthorities());
                           authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                           SecurityContextHolder.getContext().setAuthentication(authentication);
                       }
                   }
               }
           }
       }
       filterChain.doFilter(request, response);
    }
}
