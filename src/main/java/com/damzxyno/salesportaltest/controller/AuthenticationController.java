package com.damzxyno.salesportaltest.controller;

import com.damzxyno.salesportaltest.dto.AuthRequestDTO;
import com.damzxyno.salesportaltest.dto.JwtResponseDTO;
import com.damzxyno.salesportaltest.dto.UserRegistrationDTO;
import com.damzxyno.salesportaltest.dto.UserRegistrationResponseDTO;
import com.damzxyno.salesportaltest.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        var authResponse = authenticationService.authenticate(authRequestDTO);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> LogOutToken(){
        var logOutResponse = authenticationService.logOut();
        return ResponseEntity.ok(logOutResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> registerNewUser(UserRegistrationDTO userRegistrationDTO){
        var logOutResponse = authenticationService.registerUser(userRegistrationDTO);
        return ResponseEntity.ok(logOutResponse);
    }


}
