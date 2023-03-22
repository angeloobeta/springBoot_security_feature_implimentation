package com.example.springsecurityclient.services;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.model.UserModel;
import com.example.springsecurityclient.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    /**
     * @param userModel
     * @return
     */

    @Autowired
    private UserRepository userRepository;
    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setPassword(user.getPassword());
        user.setRole("User");
        return null;
    }
}
