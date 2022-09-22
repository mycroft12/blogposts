package com.fdcorp.blogposts.Util;

/*
 * Copyright (c) 2020 by FDCorp, Inc., All rights reserved.
 * This source code, and resulting software, is the confidential and proprietary information
 * ("Proprietary Information") and is the intellectual property ("Intellectual Property")
 * of FDCorp Group, Inc. ("The Company"). You shall not disclose such Proprietary Information and
 * shall use it only in accordance with the terms and conditions of any and all license
 * agreements you have entered into with The Company.
 */

import com.fdcorp.blogposts.model.User;
import com.fdcorp.blogposts.model.VerificationToken;
import com.fdcorp.blogposts.repository.VerificationTokenRepository;

import java.util.UUID;

public class Utils {
    public static class UtilAuthServiceMethods {
        public static String generateVerificationToken(User user, VerificationTokenRepository verificationTokenRepository) {
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setUser(user);
            verificationToken.setToken(token);
            verificationTokenRepository.save(verificationToken);
            return token;
        }
    }

}
