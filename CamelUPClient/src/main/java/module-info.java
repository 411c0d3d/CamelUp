module com.oasisstudios.camelupclient {
    requires org.jetbrains.annotations;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.controlsfx.controls;
    requires static lombok;
    requires spring.context;
    requires spring.websocket;
    requires spring.web;
    requires spring.messaging;
    requires spring.beans;
    requires spring.core;
    requires spring.webmvc;
    requires javax.inject;
    requires org.slf4j;
    requires spring.webflux;
    requires com.fasterxml.jackson.core;
    requires reactor.core;
    requires jakarta.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires org.kordamp.ikonli.materialdesign;
    requires org.kordamp.ikonli.javafx;
    requires java.compiler;
    requires jakarta.validation;
    requires codemodel;
    requires jsonschema2pojo.core;

    opens com.oasisstudios.camelupclient.configuration to spring.context, spring.core, spring.beans, spring.webmvc, javax.inject, spring.webflux;

    opens com.oasisstudios.camelupclient.dto to com.google.gson;
    opens com.oasisstudios.camelupclient.model to javafx.base;
    exports com.oasisstudios.camelupclient.dto to com.fasterxml.jackson.databind;
    exports com.oasisstudios.camelupclient;
}
