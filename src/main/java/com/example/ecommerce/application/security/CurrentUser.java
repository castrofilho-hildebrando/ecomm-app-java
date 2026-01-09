package com.example.ecommerce.application.security;

import com.example.ecommerce.domain.user.UserId;

public interface CurrentUser {

    UserId id();
}
