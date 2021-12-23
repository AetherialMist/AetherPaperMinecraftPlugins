package aetherial.aether.essentials.exception;

/**
 * Throw when something has already been initialized and an attempt
 * to initialize again has been made, but should not have been.
 */
public class AlreadyInitialized extends RuntimeException {

    public AlreadyInitialized(String message) {
        super(message);
    }

}
