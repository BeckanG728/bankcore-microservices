package es.bytescolab.ms_accounts.utils.exception;

public class MaxAccountsReachedException extends RuntimeException {
    public MaxAccountsReachedException(String message) {
        super(message);
    }
}
