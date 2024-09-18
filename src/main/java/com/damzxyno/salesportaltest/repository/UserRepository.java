package com.damzxyno.salesportaltest.repository;

import com.damzxyno.salesportaltest.model.UserInfo;

public interface UserRepository {
    UserInfo findByUsername (String name);
}
