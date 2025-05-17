package com.oasisstudios.camelupadmin.configuration;

import com.oasisstudios.camelupadmin.GUIApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class JavaFxConfig {

    /**
     * Gui application.
     *
     * @return the gui application
     */
    @Bean
    @Scope("singleton")
    public GUIApplication guiApplication() {
        return new GUIApplication();
    }
}