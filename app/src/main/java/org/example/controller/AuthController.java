package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.entities.RefreshToken;
import org.example.model.UsrInfoDto;
import org.example.response.JWTresponseDTO;
import org.example.service.JwtService;
import org.example.service.RefreshTokenService;
import org.example.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("auth/v1/signup")
    public ResponseEntity SignUp(@RequestBody UsrInfoDto usrInfoDto){
        try{
            Boolean isSignUped = userDetailsService.signupUser(usrInfoDto);
            if(Boolean.FALSE.equals(isSignUped)){
                return new ResponseEntity<>("Already exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefershToken(usrInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(usrInfoDto.getUsername());
            return new ResponseEntity<>(JWTresponseDTO.builder().accessToken(jwtToken).token(refreshToken.
                    getToken()).build(),HttpStatus.OK);

        }catch(Exception ex){
            return new ResponseEntity<>("Exception in User Service" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
