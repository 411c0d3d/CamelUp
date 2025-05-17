package com.oasisstudios.camelupclient.configuration;

import com.oasisstudios.camelupclient.service.*;
import com.google.gson.Gson;

import com.oasisstudios.camelupclient.api.Api;
import com.oasisstudios.camelupclient.service.CurrentUserInfoService;
import com.oasisstudios.camelupclient.service.NavigationManager;
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