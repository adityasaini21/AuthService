package org.example.controller;

import org.example.entities.RefreshToken;
import org.example.request.AuthrequestDTO;
import org.example.request.RefreshTokenRequestDTO;
import org.example.response.JWTresponseDTO;
import org.example.service.JwtService;
import org.example.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;

@Controller
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    //when both jwt and refreshToken has expired
    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticateandGetToken(@RequestBody AuthrequestDTO authrequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authrequestDTO.getUsername(),authrequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefershToken(authrequestDTO.getUsername());
            return new ResponseEntity<>(JWTresponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(authrequestDTO.getUsername()))
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Exception in user Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //when only jwt token(Access token is expired)
    @PostMapping("auth/v1/refreshToken")
    public JWTresponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accesssToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JWTresponseDTO.builder()
                            .accessToken(accesssToken)
                            .token(refreshTokenRequestDTO.getToken())
                            .build();
                }).orElseThrow(() -> new RuntimeException("refresh Token is not in DB"));
    }



}
