package com.damzxyno.salesportaltest.service.interfaces;


import com.damzxyno.salesportaltest.dto.AuthRequestDTO;
import com.damzxyno.salesportaltest.dto.JwtResponseDTO;
import com.damzxyno.salesportaltest.model.UserRegistrationDTO;
import com.damzxyno.salesportaltest.model.UserRegistrationResponseDTO;

public interface AuthenticationService {
    JwtResponseDTO authenticate(AuthRequestDTO authRequestDTO);

    Object logOut();

    UserRegistrationResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO);
}
