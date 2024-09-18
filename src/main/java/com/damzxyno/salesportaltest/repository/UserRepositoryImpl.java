package com.damzxyno.salesportaltest.repository;

import com.damzxyno.salesportaltest.enums.UserRole;
import com.damzxyno.salesportaltest.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final PasswordEncoder passwordEncoder;
    private Map<String, UserInfo> repo;

    private void init (){
        if (repo == null){
            repo = new HashMap<>(Map.of("damzxyno", UserInfo.builder()
                            .id(1L)
                            .username("damzxyno")
                            .password(passwordEncoder.encode("password"))
                            .roles(Set.of(UserRole.ADMIN))
                            .build(),

                    "jabeez", UserInfo.builder()
                            .id(2L)
                            .username("jabeez")
                            .password(passwordEncoder.encode("password"))
                            .roles(Set.of(UserRole.CUSTOMER))
                            .build()));
        }
    }
    @Override
    public UserInfo findByUsername(String name) {
        init();
        return repo.get(name);
    }
}
