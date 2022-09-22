package com.fdcorp.blogposts.service.impl;

/*
 * Copyright (c) 2020 by FDCorp, Inc., All rights reserved.
 * This source code, and resulting software, is the confidential and proprietary information
 * ("Proprietary Information") and is the intellectual property ("Intellectual Property")
 * of FDCorp Group, Inc. ("The Company"). You shall not disclose such Proprietary Information and
 * shall use it only in accordance with the terms and conditions of any and all license
 * agreements you have entered into with The Company.
 */

import com.fdcorp.blogposts.Util.Utils;
import com.fdcorp.blogposts.dto.AuthenticationResponse;
import com.fdcorp.blogposts.dto.LoginRequest;
import com.fdcorp.blogposts.dto.NotificationEmail;
import com.fdcorp.blogposts.dto.RegisterRequest;
import com.fdcorp.blogposts.exception.BlogPostsException;
import com.fdcorp.blogposts.model.User;
import com.fdcorp.blogposts.model.VerificationToken;
import com.fdcorp.blogposts.repository.UserRepository;
import com.fdcorp.blogposts.repository.VerificationTokenRepository;
import com.fdcorp.blogposts.security.JwtProvider;
import com.fdcorp.blogposts.service.AuthService;
import com.fdcorp.blogposts.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    @Async
    public void signUp(RegisterRequest registerRequest) {
        Optional<User> checkIfUserExists= userRepository.findByUsername(registerRequest.getUsername());
        if(checkIfUserExists.isPresent()){
            throw new BlogPostsException("This username is already existing");
        } else{
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setCreated(Instant.now());
            user.setEnabled(false);
            userRepository.save(user);
            String token = Utils.UtilAuthServiceMethods.generateVerificationToken(user,verificationTokenRepository);
            String subject = "Please Activate your account";
            String body = "Thank you for signing up to Blog Posts "+
                    "Please click on the below url in order to activate your account : "+
                    "http://localhost:8080/api/auth/accountVerification/"+token;
            mailService.sendEmail(new NotificationEmail(subject, user.getEmail(), body));
        }
    }

    @Override
    public void verifyAccount(String token) {
        Optional<VerificationToken> optToken = verificationTokenRepository.findByToken(token);
        optToken.orElseThrow(()->new BlogPostsException("Invalid Token"));
        enableUser(optToken.get());
    }

    @Override
    public AuthenticationResponse signIn(LoginRequest loginRequest) {
       Authentication authenticate= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getLogin(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateJWTToken(authenticate);
        return new AuthenticationResponse(token,loginRequest.getLogin());
    }

    @Transactional
    private void enableUser(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User currentUser = userRepository.findByUsername(username).orElseThrow(()->
                new BlogPostsException("User not found with name: "+username));
        currentUser.setEnabled(true);
        userRepository.save(currentUser);
    }

}
