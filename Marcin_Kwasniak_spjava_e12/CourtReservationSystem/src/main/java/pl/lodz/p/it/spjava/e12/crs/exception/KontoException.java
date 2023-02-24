package pl.lodz.p.it.spjava.e12.crs.exception;

import javax.persistence.OptimisticLockException;
import pl.lodz.p.it.spjava.e12.crs.model.Account;

/**
 *
 */
public class KontoException extends AppBaseException {

    static final public String KEY_DB_CONSTRAINT = "error.konto.db.constraint.uniq.login";
    static final public String KEY_DATA_NOT_FOUND = "error.konto.db.data.not.found";
    static final public String KEY_OPTIMISTIC_LOCK = "error.konto.optimisticlock";

    private KontoException(String message) {
        super(message);
    }

    private KontoException(String message, Throwable cause) {
        super(message, cause);
    }

    private Account account;

    public Account getKonto() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = this.account;
    }

    static public KontoException createKontoExceptionWithOptimisticLockKey(Account account, OptimisticLockException cause) {
        KontoException ke = new KontoException(KEY_OPTIMISTIC_LOCK, cause);
        ke.setAccount(account);
        return ke;
    }

    static public KontoException createKontoExceptionWithTxRetryRollback() {
        KontoException ke = new KontoException(KEY_TX_RETRY_ROLLBACK);
        return ke;
    }

    static public KontoException exceptionRequiredDataIsNotAvailable() {
        KontoException ke = new KontoException(KEY_DATA_NOT_FOUND);
        return ke;
    }

    static public KontoException createWithDbCheckConstraintKey(Account account, Throwable cause) {
        KontoException ke = new KontoException(KEY_DB_CONSTRAINT, cause);
        ke.account = account;
        return ke;
    }
}
