package org.example.controller;


import lombok.AllArgsConstructor;
import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.model.UserInfoDto;
import org.example.response.JwtResponseDTO;
import org.example.service.JwtService;
import org.example.service.RefreshTokenService;
import org.example.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//bsaseURL/api/auth/signup

@RestController
@AllArgsConstructor
public class AuthController {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("auth/v1/signup")
    public ResponseEntity signUp(@RequestBody UserInfoDto userInfoDto){
        try{
            boolean isSignedUp = userDetailsService.signUpUser(userInfoDto);
            if(!isSignedUp){
                return new ResponseEntity<>("User Already exists", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken=refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken)
                    .token(refreshToken.getToken()).build(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Exception in User Service",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
