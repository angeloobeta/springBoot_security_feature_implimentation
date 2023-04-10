package com.example.springsecurityclient.controller;

import com.example.springsecurityclient.entity.UserData;
import com.example.springsecurityclient.entity.VerificationToken;
import com.example.springsecurityclient.event.RegistrationCompleteEvent;
import com.example.springsecurityclient.model.PasswordModel;
import com.example.springsecurityclient.model.UserModel;
import com.example.springsecurityclient.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class RegistrationController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserService userService;

    private final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest httpServletRequest){
        LOGGER.info("Inside registerDepartment Controller");
        UserData userData = userService.registerUser(userModel);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(userData,applicationUrl(httpServletRequest)));
        return "Success";
    }


    @GetMapping("/verifyToken")
    public String verificationToken(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);

        if(result.equalsIgnoreCase("valid")){
            return  "User verified successfully";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest httpServletRequest){
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        UserData userData = verificationToken.getUserData();
        resendVerificationTokenMail(userData, applicationUrl(httpServletRequest), verificationToken);
        return "Verification link sent";


    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest httpServletRequest){
        UserData userData = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(userData != null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(userData, token);
            url = passwordResetTokenMailer(userData,applicationUrl(httpServletRequest), token);
        }
        return url;
    }

    private String passwordResetTokenMailer(UserData userData, String applicationUrl, String token) {
        // Send mail to user
        String url = applicationUrl + "/savePassword?token=" + token;
        // send verification to email
        LOGGER.info("Click the link to reset your password: {}", url);
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")){
            return "Invalid token";
        }
        Optional<UserData> userData = userService.getUserByPasswordResetToken(token);

        if(userData.isPresent()){
            userService.changePassword(userData.get(), passwordModel.getNewPassword());
            return "Password reset successfully";
        }else{
            return "Invalid token";
        }
    }

    private void resendVerificationTokenMail(UserData userData, String applicationUrl, VerificationToken verificationToken) {
        // Send mail to user
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken;
        // send verification to email
        LOGGER.info("Click the link to verify your account: {}", url);
    }

    private String applicationUrl(HttpServletRequest httpServletRequest ){
        return "https://" +
                httpServletRequest.getServerName()+
                httpServletRequest.getServerPort()+
                httpServletRequest.getContextPath();
    }
}
