package com.damzxyno.salesportaltest.model;

import com.damzxyno.salesportaltest.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private long id;
    private String username;
    @JsonIgnore
    private String password;
    private Set<UserRole> roles = new HashSet<>();
}
