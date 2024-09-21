package com.damzxyno.salesportaltest.service.interfaces;


import com.damzxyno.salesportaltest.dto.AuthRequestDTO;
import com.damzxyno.salesportaltest.dto.JwtResponseDTO;
import com.damzxyno.salesportaltest.dto.UserRegistrationDTO;
import com.damzxyno.salesportaltest.dto.UserRegistrationResponseDTO;

public interface AuthenticationService {
    JwtResponseDTO authenticate(AuthRequestDTO authRequestDTO);

    Object logOut();

    UserRegistrationResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO);
}
