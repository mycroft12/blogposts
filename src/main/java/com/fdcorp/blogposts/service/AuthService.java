package com.fdcorp.blogposts.service;

/*
 * Copyright (c) 2020 by FDCorp, Inc., All rights reserved.
 * This source code, and resulting software, is the confidential and proprietary information
 * ("Proprietary Information") and is the intellectual property ("Intellectual Property")
 * of FDCorp Group, Inc. ("The Company"). You shall not disclose such Proprietary Information and
 * shall use it only in accordance with the terms and conditions of any and all license
 * agreements you have entered into with The Company.
 */

import com.fdcorp.blogposts.dto.AuthenticationResponse;
import com.fdcorp.blogposts.dto.LoginRequest;
import com.fdcorp.blogposts.dto.RegisterRequest;

public interface AuthService {
    void signUp(RegisterRequest registerRequest);

    void verifyAccount(String token);

    AuthenticationResponse signIn(LoginRequest loginRequest);
}
