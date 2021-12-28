package com.github.aetherialmist.aether.essentials.exception;

/**
 * Throw when something has not been initialized and an attempt
 * to use it has been made, but should have been initialized first.
 * <p>
 * Can be interpreted as the opposite of {@link AlreadyInitialized}
 */
public class NotInitialized extends RuntimeException {

    private static final String HAS_NOT_BEEN_INITIALIZED_YET = " has not been initialized yet";

    /**
     * Creates this exception with a custom message
     *
     * @param message The message to include in this exception
     */
    public NotInitialized(String message) {
        super(message);
    }

    /**
     * Create this exception using the given class's name and a default message template
     *
     * @param clazz The class that has not been initialized yet
     */
    public NotInitialized(Class<?> clazz) {
        super(clazz.getName() + HAS_NOT_BEEN_INITIALIZED_YET);
    }

}
