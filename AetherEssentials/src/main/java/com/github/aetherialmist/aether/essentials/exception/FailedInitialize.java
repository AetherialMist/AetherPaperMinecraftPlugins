package com.github.aetherialmist.aether.essentials.exception;

/**
 * Throw when something has failed to initialize
 */
public class FailedInitialize extends RuntimeException {

    private static final String HAS_FAILED_TO_INITIALIZE = " has failed to initialize";

    /**
     * Create with a custom message
     *
     * @param message The message to include in this exception
     */
    public FailedInitialize(String message) {
        super(message);
    }

    /**
     * Create this exception using the given class's name in a default message template
     *
     * @param clazz The class that failed to initialize
     */
    public FailedInitialize(Class<?> clazz) {
        super(clazz.getName() + HAS_FAILED_TO_INITIALIZE);
    }

}
