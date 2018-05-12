package cft.sample.app.exceptions;

/**
 * Error codes used to represent detailed information about errors
 */

public enum ErrCodes {

    /**
     * New item id is less that cached one
     */
    ORDERING_ERROR("Ordering error"),

    /**
     * Too many item ids for some group id
     */
    QUEUE_MAX_SIZE_EXCEEDED("A separate queue max size exceeded");

    private String message;

    ErrCodes(String mess) {
        this.message = mess;
    }

    public String getMess() {
        return message;
    }
}
