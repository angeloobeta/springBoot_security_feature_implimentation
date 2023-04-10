package com.example.springsecurityclient.services;

import com.example.springsecurityclient.entity.UserData;
import com.example.springsecurityclient.entity.VerificationToken;
import com.example.springsecurityclient.model.UserModel;

import java.util.Optional;

public interface UserService {
    UserData registerUser(UserModel userModel);

    void saveVerificationToken(String token, UserData userData);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    UserData findUserByEmail(String email);

    void createPasswordResetTokenForUser(UserData userData, String token);

    String validatePasswordResetToken(String token);

    Optional<UserData> getUserByPasswordResetToken(String token);

    void changePassword(UserData userData, String newPassword);

    boolean checkIfValidOldPassword(UserData userData, String oldPassword);
}
