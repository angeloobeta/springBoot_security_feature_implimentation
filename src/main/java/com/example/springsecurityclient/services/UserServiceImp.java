package com.example.springsecurityclient.services;

import com.example.springsecurityclient.entity.PasswordResetToken;
import com.example.springsecurityclient.entity.UserData;
import com.example.springsecurityclient.entity.VerificationToken;
import com.example.springsecurityclient.model.UserModel;
import com.example.springsecurityclient.repository.PasswordResetTokenRepository;
import com.example.springsecurityclient.repository.UserRepository;
import com.example.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserData registerUser(UserModel userModel) {
        UserData userData = new UserData();
        userData.setEmail(userModel.getEmail());
        userData.setFirstName(userModel.getFirstName());
        userData.setLastName(userModel.getLastName());
        userData.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userData.setRole("User");
        return userRepository.save(userData);
    }

    /**
     * @param token
     * @param userData
     */
    @Override
    public void saveVerificationToken(String token, UserData userData) {
        VerificationToken verificationToken = new VerificationToken(userData, token);
        verificationTokenRepository.save(verificationToken);

    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);



        if(verificationToken == null){
            return "invalid token";
        }

        UserData userData = verificationToken.getUserData();
        Calendar calendar = Calendar.getInstance();

        if((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0)){
            verificationTokenRepository.delete(verificationToken);
            return "token expired";
        }

        userData.setEnabled(true);
        userRepository.save(userData);
        return "valid token";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;

    }

    @Override
    public UserData findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(UserData userData, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(userData, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);



        if(passwordResetToken == null){
            return "invalid token";
        }

        UserData userData = passwordResetToken.getUserData();
        Calendar calendar = Calendar.getInstance();

        if((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0)){
            passwordResetTokenRepository.delete(passwordResetToken);
            return "token expired";
        }

        return "valid token";
    }

    @Override
    public Optional<UserData> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUserData());
    }

    @Override
    public void changePassword(UserData userData, String newPassword) {
        userData.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userData);
    }
}
