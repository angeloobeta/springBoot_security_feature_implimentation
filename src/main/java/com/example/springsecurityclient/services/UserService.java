package com.example.springsecurityclient.services;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.model.UserModel;
import org.springframework.stereotype.Service;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationToken(String token, User user);
}
