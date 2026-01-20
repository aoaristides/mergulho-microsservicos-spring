package br.com.makersweb.delivery.tracking.domain.exception;

/**
 * @author andersonorione
 */
public class DomainException extends RuntimeException {

    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
