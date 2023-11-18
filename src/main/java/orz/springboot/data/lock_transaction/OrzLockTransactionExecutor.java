package orz.springboot.data.lock_transaction;

import lombok.SneakyThrows;
import orz.springboot.data.lock.OrzLockExecutor;
import orz.springboot.data.transaction.OrzTransactionExecutor;

public class OrzLockTransactionExecutor {
    private final OrzLockExecutor lockExecutor;
    private final OrzTransactionExecutor transactionExecutor;

    public OrzLockTransactionExecutor(OrzLockExecutor lockExecutor, OrzTransactionExecutor transactionExecutor) {
        this.lockExecutor = lockExecutor;
        this.transactionExecutor = transactionExecutor;
    }

    public <E1 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> exceptionClass) throws E1 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> e1, Class<E2> e2) throws E1, E2 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3) throws E1, E2, E3 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4) throws E1, E2, E3, E4 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5) throws E1, E2, E3, E4, E5 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception, E6 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6) throws E1, E2, E3, E4, E5, E6 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception, E6 extends Exception, E7 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7) throws E1, E2, E3, E4, E5, E6, E7 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception, E6 extends Exception, E7 extends Exception, E8 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, Class<E8> e8) throws E1, E2, E3, E4, E5, E6, E7, E8 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception, E6 extends Exception, E7 extends Exception, E8 extends Exception, E9 extends Exception> OrzLockTransactionExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, Class<E8> e8, Class<E9> e9) throws E1, E2, E3, E4, E5, E6, E7, E8, E9 {
        return this;
    }

    @SneakyThrows
    public void exec(OrzTransactionExecutor.Action1 action) {
        lockExecutor.exec(() -> transactionExecutor.exec(action));
    }

    @SneakyThrows
    public <R> R exec(OrzTransactionExecutor.Action2<R> action) {
        return lockExecutor.exec(() -> transactionExecutor.exec(action));
    }

    @SneakyThrows
    public <P> void exec(OrzTransactionExecutor.Action3<P> action, P param) {
        lockExecutor.exec(() -> transactionExecutor.exec(() -> action.run(param)));
    }

    @SneakyThrows
    public <P, R> R exec(OrzTransactionExecutor.Action4<P, R> action, P param) {
        return lockExecutor.exec(() -> transactionExecutor.exec(() -> action.run(param)));
    }
}
