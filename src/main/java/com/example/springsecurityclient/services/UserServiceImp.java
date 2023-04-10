package com.example.springsecurityclient.services;

import com.example.springsecurityclient.entity.UserData;
import com.example.springsecurityclient.entity.VerificationToken;
import com.example.springsecurityclient.model.UserModel;
import com.example.springsecurityclient.repository.UserRepository;
import com.example.springsecurityclient.repository.VerificationTokenRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

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
}
