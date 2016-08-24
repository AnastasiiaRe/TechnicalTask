package framework.platform;

/**
 * Utilitarian class which specifies exceptions that can be caused by incorrect input of browser type.
 */
public class UnknownBrowserException extends RuntimeException {
    public UnknownBrowserException(String message) {
        super(message);
    }
}
