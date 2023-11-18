package orz.springboot.data.lock_transaction;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import orz.springboot.data.lock.OrzLockManager;
import orz.springboot.data.lock.OrzLockName;
import orz.springboot.data.transaction.OrzTransaction;
import orz.springboot.data.transaction.OrzTransactionManager;

@Component
@ConditionalOnBean({OrzLockManager.class, OrzTransactionManager.class})
public class OrzLockTransactionManager {
    private final OrzLockManager lockManager;
    private final OrzTransactionManager transactionManager;

    public OrzLockTransactionManager(OrzLockManager lockManager, OrzTransactionManager transactionManager) {
        this.lockManager = lockManager;
        this.transactionManager = transactionManager;
    }

    public OrzLockTransactionExecutor executor(OrzLockName lockName) {
        return new OrzLockTransactionExecutor(lockManager.executor(lockName), transactionManager.executor());
    }

    public OrzLockTransactionExecutor executor(OrzLockName lockName, OrzTransaction transaction) {
        return new OrzLockTransactionExecutor(lockManager.executor(lockName), transactionManager.executor(transaction));
    }
}
