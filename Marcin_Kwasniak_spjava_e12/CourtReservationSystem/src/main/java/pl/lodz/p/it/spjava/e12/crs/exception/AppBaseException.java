package pl.lodz.p.it.spjava.e12.crs.exception;

import javax.ejb.ApplicationException;

/**
 * Klasa bazowego wyjątku aplikacyjnego
 */
@ApplicationException(rollback = true)
abstract public class AppBaseException extends Exception {

    static final public String KEY_TX_RETRY_ROLLBACK = "error.tx.retry.rollback";

    protected AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AppBaseException(String message) {
        super(message);
    }
}
