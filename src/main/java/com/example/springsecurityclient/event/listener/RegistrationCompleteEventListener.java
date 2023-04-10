package com.example.springsecurityclient.event.listener;

import com.example.springsecurityclient.entity.UserData;
import com.example.springsecurityclient.event.RegistrationCompleteEvent;
import com.example.springsecurityclient.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create the verification token for the user with a link
        UserData userData = event.getUserData();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationToken(token, userData);

        // Send mail to user
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;
        // send verification to email
        log.info("Click the link to verify your account: {}", url);


    }
}
