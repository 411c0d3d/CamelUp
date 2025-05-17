package com.oasisstudios.camelupclient.model;

import com.oasisstudios.camelupclient.service.UserType;
import lombok.Getter;

@Getter
public class User {
    private final UserType userType;
    private final String userName;
    private final String userId;
    
    public User(String Id, String userName, UserType userType) {
        this.userType = userType;
        this.userName = userName;
        this.userId = Id;
    }
}
