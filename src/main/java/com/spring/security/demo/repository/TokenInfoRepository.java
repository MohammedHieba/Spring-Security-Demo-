package com.spring.security.demo.repository;

import com.spring.security.demo.model.Role;
import com.spring.security.demo.model.TokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenInfoRepository extends JpaRepository<TokenInfo, Long> {
    Optional<TokenInfo> findByAccessToken(String accessToken);
    Optional<TokenInfo> findByRefreshToken(String refreshToken);
}
