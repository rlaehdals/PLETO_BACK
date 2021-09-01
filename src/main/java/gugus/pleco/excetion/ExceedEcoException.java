package gugus.pleco.excetion;

public class ExceedEcoException extends RuntimeException {
    public ExceedEcoException() {
        super();
    }

    public ExceedEcoException(String message) {
        super(message);
    }

    public ExceedEcoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceedEcoException(Throwable cause) {
        super(cause);
    }

}
