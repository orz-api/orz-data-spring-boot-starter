package orz.springboot.data;

import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import orz.springboot.base.OrzBaseUtils;

public class OrzLock {
    private final RLock lock;

    public OrzLock(RLock lock) {
        this.lock = lock;
    }

    public static OrzLock of(String name) {
        return OrzLock.of(name, false);
    }

    public static OrzLock of(String name, boolean shared) {
        var redissonClient = OrzBaseUtils.getAppContext().getBean(RedissonClient.class);
        return OrzLock.of(redissonClient, name, shared);
    }

    public static OrzLock of(RedissonClient redissonClient, String name) {
        return OrzLock.of(redissonClient, name, false);
    }

    public static OrzLock of(RedissonClient redissonClient, String name, boolean shared) {
        var prefix = "orz-lock:";
        if (!shared) {
            prefix = OrzBaseUtils.getAppContext().getEnvironment().getRequiredProperty("spring.application.name") + ":" + prefix;
        }
        return OrzLock.of(redissonClient.getLock(prefix + name));
    }

    public static OrzLock of(RLock lock) {
        return new OrzLock(lock);
    }

    public OrzTransaction transaction() {
        return OrzTransaction.of(this);
    }

    public OrzTransaction transaction(String manager) {
        return OrzTransaction.of(manager, this);
    }

    public <E extends Exception> void exec(Action1<E> action) throws E {
        exec((Action4<Void, Void, E>) p -> {
            action.run();
            return null;
        }, null);
    }

    public <R, E extends Exception> R exec(Action2<R, E> action) throws E {
        return exec((Action4<Void, R, E>) p -> action.run(), null);
    }

    public <P, E extends Exception> void exec(Action3<P, E> action, P param) throws E {
        exec((Action4<P, Void, E>) p -> {
            action.run(param);
            return null;
        }, null);
    }

    @SneakyThrows
    public <P, R, E extends Exception> R exec(Action4<P, R, E> action, P param) throws E {
        try {
            lock.lock();
            return action.run(param);
        } catch (OrzLockException e) {
            throw e.getException();
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public interface Action1<E extends Exception> {
        void run() throws OrzLockException, E;
    }

    public interface Action2<R, E extends Exception> {
        R run() throws OrzLockException, E;
    }

    public interface Action3<P, E extends Exception> {
        void run(P param) throws OrzLockException, E;
    }

    public interface Action4<P, R, E extends Exception> {
        R run(P param) throws OrzLockException, E;
    }
}
