package com.oasisstudios.camelupclient.service;

import com.oasisstudios.camelupclient.model.*;

import org.springframework.stereotype.Component;


/**
 * The Current user info service.
 */
@Component
public class CurrentUserInfoService {

    private User user;

    /**
     * Initialize user info.
     *
     * @param userId   the user id
     * @param userName the username
     * @param userType the user type
     */
    public void initializeUserInfo(String userId, String userName, UserType userType) {
        user = new User(userId,userName,userType);
    }

    /**
     * Gets user type.
     *
     * @return the user type
     */
    public UserType getUserType() {
        return user.getUserType();
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUserName() {
        return user.getUserName();
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return user.getUserId();
    }

    /**
     * Update user info.
     *
     * @param userId   the user id
     * @param userName the username
     * @param userType the user type
     */
    public void updateUserInfo(String userId, String userName, UserType userType) {
        initializeUserInfo(userId,userName,userType);
    }
}