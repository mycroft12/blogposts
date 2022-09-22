package com.fdcorp.blogposts.security;

/*
 * Copyright (c) 2022 by FDCorp, Inc., All rights reserved.
 * This source code, and resulting software, is the confidential and proprietary information
 * ("Proprietary Information") and is the intellectual property ("Intellectual Property")
 * of FDCorp Group, Inc. ("The Company"). You shall not disclose such Proprietary Information and
 * shall use it only in accordance with the terms and conditions of any and all license
 * agreements you have entered into with The Company.
 */

import com.fdcorp.blogposts.exception.BlogPostsException;
import com.fdcorp.blogposts.model.User;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    public String generateJWTToken(Authentication authentication){
        User principal = (User)authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private Key getPrivateKey() {
        try{
            return (PrivateKey) keyStore.getKey("blogkeypost","secretkey".toCharArray());
        }catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException keyStoreException){
            throw new BlogPostsException("Exception occured while retrieving public key from keystore");
        }
    }
}
