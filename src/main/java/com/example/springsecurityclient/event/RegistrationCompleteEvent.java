package com.example.springsecurityclient.event;

import com.example.springsecurityclient.entity.UserData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private UserData userData;
    private String applicationUrl;
    public RegistrationCompleteEvent(UserData userData, String applicationUrl) {
        super(userData);
        this.applicationUrl = applicationUrl;
        this.userData = userData;
    }

}
