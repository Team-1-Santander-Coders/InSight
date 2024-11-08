package getterson.insight.exceptions;

public class WrappedTopicNotFoundException extends RuntimeException {
    public WrappedTopicNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
