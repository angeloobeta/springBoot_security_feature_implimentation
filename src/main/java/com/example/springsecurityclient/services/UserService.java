package com.example.springsecurityclient.services;

import com.example.springsecurityclient.entity.UserData;
import com.example.springsecurityclient.entity.VerificationToken;
import com.example.springsecurityclient.model.UserModel;

public interface UserService {
    UserData registerUser(UserModel userModel);

    void saveVerificationToken(String token, UserData userData);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);
}
