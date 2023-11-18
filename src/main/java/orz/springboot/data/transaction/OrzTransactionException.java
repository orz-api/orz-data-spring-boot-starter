package orz.springboot.data.transaction;

import lombok.Getter;

@Getter
public class OrzTransactionException extends Exception {
    private final Exception exception;
    private final boolean rollback;

    public OrzTransactionException(Exception exception) {
        this(exception, true);
    }

    public OrzTransactionException(Exception exception, boolean rollback) {
        super(exception);
        this.exception = exception;
        this.rollback = rollback;
    }

    public static OrzTransactionException of(Exception exception) {
        return new OrzTransactionException(exception);
    }

    public static OrzTransactionException of(Exception exception, boolean rollback) {
        return new OrzTransactionException(exception, rollback);
    }
}
