package com.github.aetherialmist.aether.essentials.exception;

/**
 * Throw when something has already been initialized and an attempt
 * to initialize again has been made, but should not have been.
 * <p>
 * Can be interpreted as the opposite of {@link NotInitialized}
 */
public class AlreadyInitialized extends RuntimeException {

    public static final String HAS_ALREADY_BEEN_INITIALIZED = " has already been initialized";

    /**
     * Create this exception with a custom message
     *
     * @param message The message to include in this exception
     */
    public AlreadyInitialized(String message) {
        super(message);
    }

    /**
     * Create this exception using the given class's name in a default message template
     *
     * @param clazz The class that has already been initialized
     */
    public AlreadyInitialized(Class<?> clazz) {
        super(clazz.getName() + HAS_ALREADY_BEEN_INITIALIZED);
    }

}
