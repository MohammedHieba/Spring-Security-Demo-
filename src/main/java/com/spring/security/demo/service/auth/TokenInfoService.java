package com.spring.security.demo.service.auth;


import com.spring.security.demo.model.TokenInfo;
import com.spring.security.demo.repository.TokenInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TokenInfoService {

    @Autowired
    private TokenInfoRepository tokenInfoRepository;


    public TokenInfo save(TokenInfo tokenInfo){
       return this.tokenInfoRepository.save(tokenInfo);
    }

}
