package orz.springboot.data;

import lombok.Getter;

@Getter
public class OrzTransactionException extends Exception {
    private final Exception exception;
    private final boolean rollback;

    public OrzTransactionException(Exception exception) {
        this.exception = exception;
        this.rollback = true;
    }

    public OrzTransactionException(Exception exception, boolean rollback) {
        this.exception = exception;
        this.rollback = rollback;
    }
}
