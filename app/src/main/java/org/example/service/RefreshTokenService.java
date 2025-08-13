package org.example.service;

import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserInfoRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        UserInfo userInfoExtracted = userRepository.findByUsername(username);
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userInfoExtracted)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(6000000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }
    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
    public boolean isExpired(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())>0){
            return false;
        }
        return true;
    }

    public RefreshToken getToken(String username){
        UserInfo userInfo=userRepository.findByUsername(username);
        RefreshToken token=refreshTokenRepository.findByUserInfo_UserId(userInfo.getUserId()).orElse(null);
        return token;
    }


    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

}
