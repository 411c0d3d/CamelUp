package com.oasisstudios.camelupadmin.configuration;

import com.oasisstudios.camelupadmin.service.*;
import com.google.gson.Gson;

import com.oasisstudios.camelupadmin.api.Api;
import com.oasisstudios.camelupadmin.service.CurrentUserInfoService;
import com.oasisstudios.camelupadmin.service.NavigationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Central configuration class for the application.
 * This class defines the Beans used throughout the application.
 */
@Configuration
public class AppConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    
    /*
     * Creates and provides the Api for Client-Server Communication.
     * This bean is defined with singleton scope to ensure a single instance.
     *
     * @return an instance of Api.
     */
    @Bean
    @Scope("singleton")
    public Api api() {
        return new Api(new Gson());
    }
    
    /**
     * Creates and provides the CurrentUserInfoService bean.
     * This bean is defined with singleton scope to ensure a single instance.
     *
     * @return an instance of CurrentUserInfoService
     */
    @Bean
    @Scope("singleton")
    public CurrentUserInfoService currentUserInfoService() {
        return new CurrentUserInfoService();
    }

    @Bean
    @Scope("singleton")
    public CurrentLobbyService currentLobbyService() {
        return new CurrentLobbyService();
    }
    
    /**
     * Navigation Manager service.
     *
     * @return the current Navigation Manager instance
     */
    @Bean
    @Scope("singleton")
    public NavigationManager navigationManager() {
        return new NavigationManager();
    }
}