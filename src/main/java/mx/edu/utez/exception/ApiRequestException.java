package mx.edu.utez.exception;

public class ApiRequestException extends Exception {

    private static final long serialVersionUID = 1L;

    public ApiRequestException(Exception message) {
        super(message);
    }
}
