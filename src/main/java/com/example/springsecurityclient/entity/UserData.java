package com.example.springsecurityclient.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
//@Table(name = "users")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @Column(length = 60) private String password;
    private String role;
    boolean enabled = false;

}
