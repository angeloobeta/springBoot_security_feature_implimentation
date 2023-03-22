package com.example.springsecurityclient.event.listener;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.event.RegistrationCompleteEvent;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    /**
     * @param event
     */
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create the verification token for the user with a link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        // Send mail to user


    }
}
