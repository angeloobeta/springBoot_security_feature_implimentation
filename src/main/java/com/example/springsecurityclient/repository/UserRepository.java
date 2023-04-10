package com.example.springsecurityclient.repository;

import com.example.springsecurityclient.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Long> {
    UserData findByEmail(String email);
}
