package com.example.springsecurityclient.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class VerificationToken {

//    Expiration time is 10minutes
    private static final int EXPIRATION_TIME = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name="user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private UserData userData;

    public VerificationToken(UserData userData, String token){
        this.userData = userData;
        this.token = token;
        this.expirationTime = calculatedExpirationTime(EXPIRATION_TIME);
    }

    private Date calculatedExpirationTime(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expirationTime);
        return new Date(calendar.getTime().getTime());
    }

    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculatedExpirationTime(EXPIRATION_TIME);
    }
}
