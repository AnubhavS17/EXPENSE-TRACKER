package org.example.controller;

import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.request.AuthRequestDTO;
import org.example.request.RefreshTokenRequestDTO;
import org.example.response.JwtResponseDTO;
import org.example.service.JwtService;
import org.example.service.RefreshTokenService;
import org.example.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("auth/v1/login")
    public ResponseEntity<?> AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken token=refreshTokenService.getToken(authRequestDTO.getUsername());
            if(!refreshTokenService.isExpired(token)){
                return new ResponseEntity<>("Logged In Successfully", HttpStatus.OK);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());

                return new ResponseEntity<>(JwtResponseDTO.builder()
                        .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                        .token(refreshToken.getToken())
                        .build(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("auth/v1/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        System.out.println("Received refresh token: " + refreshTokenRequestDTO.getToken());
        try {
            JwtResponseDTO response = refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(refreshToken -> {
                        UserInfo userInfo = refreshToken.getUserInfo();
                        String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(userInfo.getUsername());
                        refreshTokenService.delete(refreshToken);

                        return JwtResponseDTO.builder()
                                .accessToken(accessToken)
                                .token(newRefreshToken.getToken())
                                .build();
                    }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
//            return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
//                    .map(refreshTokenService::verifyExpiration)
//                    .map(refreshToken -> {
//                        System.out.println("Inside map block - refresh token found: " + refreshToken.getToken());
//                        UserInfo userInfo = refreshToken.getUserInfo();
//
//                        // Generate new access token
//                        String accessToken = jwtService.GenerateToken(userInfo.getUsername());
//
//                        // Generate new refresh token and save it
//                        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(userInfo.getUsername());
//
//                        // Optionally delete or revoke old refresh token here
//                        refreshTokenService.delete(refreshToken);
//
//                        // Return new access token and new refresh token string
//                        return JwtResponseDTO.builder()
//                                .accessToken(accessToken)
//                                .token(newRefreshToken.getToken())
//                                .build();
//                    }).orElseThrow(() -> {
//                        System.out.println("Refresh Token not found in DB");
//                        return new RuntimeException("Refresh Token is not in DB..!!");
//                    });
////                    .map(RefreshToken::getUserInfo)
////                    .map(userInfo -> {
////                        String accessToken = jwtService.GenerateToken(userInfo.getUsername());
////                        return JwtResponseDTO.builder()
////                                .accessToken(accessToken)
////                                .token(refreshTokenRequestDTO.getToken()).build();
////                    }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
//
//




    }
}
