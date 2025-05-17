package com.oasisstudios.camelupclient.service;

import com.oasisstudios.camelupclient.configuration.AppConfig;
import com.oasisstudios.camelupclient.configuration.JavaFxConfig;
import lombok.Getter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The Context provider.
 */
public class ContextProvider {
    
    @Getter
    private static AnnotationConfigApplicationContext context;

    /**
     * Initialize context annotation config application context.
     *
     * @return the annotation config application context
     */
    public static AnnotationConfigApplicationContext initializeContext() {
        try {
            if (context == null) {
                context = new AnnotationConfigApplicationContext(AppConfig.class, JavaFxConfig.class);
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize Spring context: " + e.getMessage());
            throw e;
        }
        return context;
    }

    /**
     * Gets bean.
     *
     * @param <T>       the type parameter
     * @param beanClass the bean class
     * @return the bean
     */
    public static <T> T getBean(Class<T> beanClass) {
        if (context == null) {
            throw new IllegalStateException("ApplicationContext is not initialized yet.");
        }
        return context.getBean(beanClass);
    }

    /**
     * Gets bean.
     *
     * @param beanName the bean name
     * @return the bean
     */
    public static Object getBean(String beanName) {
        if (context == null) {
            throw new IllegalStateException("ApplicationContext is not initialized yet.");
        }
        return context.getBean(beanName);
    }

    /**
     * Close context.
     */
    public static void closeContext() {
        if (context != null) {
            context.close();
        }
    }
}
