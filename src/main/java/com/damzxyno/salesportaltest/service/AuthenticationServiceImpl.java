package com.damzxyno.salesportaltest.service;

import com.damzxyno.salesportaltest.dto.AuthRequestDTO;
import com.damzxyno.salesportaltest.dto.JwtResponseDTO;
import com.damzxyno.salesportaltest.model.UserRegistrationDTO;
import com.damzxyno.salesportaltest.model.UserRegistrationResponseDTO;
import com.damzxyno.salesportaltest.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
//    private final UserDetailsService userDetailsService;

    @Override
    public JwtResponseDTO authenticate(AuthRequestDTO authRequestDTO) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            return JwtResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authRequestDTO.getUsername(), authentication.getAuthorities())).build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @Override
    public Object logOut() {
        return null;
    }

    @Override
    public UserRegistrationResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        return null;
    }
}
