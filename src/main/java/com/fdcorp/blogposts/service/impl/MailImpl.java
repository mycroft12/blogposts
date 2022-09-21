package com.fdcorp.blogposts.service.impl;

/*
 * Copyright (c) 2022 by FDCorp, Inc., All rights reserved.
 * This source code, and resulting software, is the confidential and proprietary information
 * ("Proprietary Information") and is the intellectual property ("Intellectual Property")
 * of FDCorp Group, Inc. ("The Company"). You shall not disclose such Proprietary Information and
 * shall use it only in accordance with the terms and conditions of any and all license
 * agreements you have entered into with The Company.
 */

import com.fdcorp.blogposts.dto.NotificationEmail;
import com.fdcorp.blogposts.exception.BlogPostsException;
import com.fdcorp.blogposts.service.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@AllArgsConstructor
@Slf4j
public class MailImpl implements MailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("blogpostsadmin@mail.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation mail sent succesfully!");
        }catch(MailException mailException){
            log.error("Error while sending an email ",mailException);
            throw new BlogPostsException("Error while sending an email: "+mailException);
        }
    }

    public String build(String message) {
        Context context= new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate",context);
    }

}
