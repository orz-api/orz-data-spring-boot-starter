package orz.springboot.data.lock;

import lombok.SneakyThrows;

public class OrzLockExecutor {
    private final OrzLock lock;

    OrzLockExecutor(OrzLock lock) {
        this.lock = lock;
    }

    public <E1 extends Exception> OrzLockExecutor exceptions(Class<E1> exceptionClass) throws E1 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception> OrzLockExecutor exceptions(Class<E1> e1, Class<E2> e2) throws E1, E2 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception> OrzLockExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3) throws E1, E2, E3 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception> OrzLockExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4) throws E1, E2, E3, E4 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception> OrzLockExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5) throws E1, E2, E3, E4, E5 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception, E6 extends Exception> OrzLockExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6) throws E1, E2, E3, E4, E5, E6 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception, E6 extends Exception, E7 extends Exception> OrzLockExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7) throws E1, E2, E3, E4, E5, E6, E7 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception, E6 extends Exception, E7 extends Exception, E8 extends Exception> OrzLockExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, Class<E8> e8) throws E1, E2, E3, E4, E5, E6, E7, E8 {
        return this;
    }

    public <E1 extends Exception, E2 extends Exception, E3 extends Exception, E4 extends Exception, E5 extends Exception, E6 extends Exception, E7 extends Exception, E8 extends Exception, E9 extends Exception> OrzLockExecutor exceptions(Class<E1> e1, Class<E2> e2, Class<E3> e3, Class<E4> e4, Class<E5> e5, Class<E6> e6, Class<E7> e7, Class<E8> e8, Class<E9> e9) throws E1, E2, E3, E4, E5, E6, E7, E8, E9 {
        return this;
    }

    @SneakyThrows
    public void exec(Action1 action) {
        try {
            lock.lock();
            action.run();
        } catch (OrzLockException e) {
            throw e.getException();
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public <R> R exec(Action2<R> action) {
        try {
            lock.lock();
            return action.run();
        } catch (OrzLockException e) {
            throw e.getException();
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public <P> void exec(Action3<P> action, P param) {
        try {
            lock.lock();
            action.run(param);
        } catch (OrzLockException e) {
            throw e.getException();
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public <P, R> R exec(Action4<P, R> action, P param) {
        try {
            lock.lock();
            return action.run(param);
        } catch (OrzLockException e) {
            throw e.getException();
        } finally {
            lock.unlock();
        }
    }

    public interface Action1 {
        void run() throws OrzLockException;
    }

    public interface Action2<R> {
        R run() throws OrzLockException;
    }

    public interface Action3<P> {
        void run(P param) throws OrzLockException;
    }

    public interface Action4<P, R> {
        R run(P param) throws OrzLockException;
    }
}
