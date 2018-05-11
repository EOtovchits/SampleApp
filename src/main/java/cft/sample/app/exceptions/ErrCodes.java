package cft.sample.app.exceptions;

public enum ErrCodes {

    ORDERING_ERROR("Ordering error"),
    QUEUE_MAX_SIZE_EXCEEDED("A separate queue max size exceeded");

    private String message;

    ErrCodes(String mess) {
        this.message = mess;
    }

    public String getMess() {
        return message;
    }
}
