package orz.springboot.data;

import lombok.Getter;

@Getter
public class OrzLockException extends Exception {
    private final Exception exception;

    public OrzLockException(Exception exception) {
        super(exception);
        this.exception = exception;
    }
}
