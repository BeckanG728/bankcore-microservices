package es.bytescolab.ms_accounts.utils.exception;

public class CustomerInactiveException extends RuntimeException {
    public CustomerInactiveException(String message) {
        super(message);
    }
}
