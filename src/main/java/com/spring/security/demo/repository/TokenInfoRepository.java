package com.spring.security.demo.repository;

import com.spring.security.demo.model.Role;
import com.spring.security.demo.model.TokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenInfoRepository extends JpaRepository<TokenInfo, Long> {
}
