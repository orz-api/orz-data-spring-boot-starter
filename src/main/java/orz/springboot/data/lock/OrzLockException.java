package orz.springboot.data.lock;

import lombok.Getter;

@Getter
public class OrzLockException extends Exception {
    private final Exception exception;

    public OrzLockException(Exception exception) {
        super(exception);
        this.exception = exception;
    }

    public static OrzLockException of(Exception exception) {
        return new OrzLockException(exception);
    }
}
