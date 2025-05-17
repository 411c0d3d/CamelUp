package com.oasisstudios.camelupadmin.api;

/**
 * Exception to determine if an event handler was already set.
 * Helps to not accidentally overwrite an event handler in event driven applications
 */
public class EventHandlerAlreadySetException extends IllegalArgumentException {
    public EventHandlerAlreadySetException() {
        super("Event handler is already set. Only one event handler is supported.");
    }
}
