package com.example.springsecurityclient.services;

import com.example.springsecurityclient.entity.User;
import com.example.springsecurityclient.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);
}
