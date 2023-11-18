package orz.springboot.data.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public class OrzLock {
    private final RLock lock;

    public OrzLock(RLock lock) {
        this.lock = lock;
    }

    public void lock() {
        lock.lock();
    }

    public boolean tryLock() {
        return lock.tryLock();
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(time, unit);
    }

    public void unlock() {
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
