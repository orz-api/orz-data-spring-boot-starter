package orz.springboot.data.transaction;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

@Component
@ConditionalOnBean(PlatformTransactionManager.class)
public class OrzTransactionManager {
    private final PlatformTransactionManager transactionManager;
    private final TransactionDefinition transactionDefinition;

    public OrzTransactionManager(PlatformTransactionManager transactionManager, TransactionDefinition transactionDefinition) {
        this.transactionManager = transactionManager;
        this.transactionDefinition = transactionDefinition;
    }

    public OrzTransaction getTransaction() {
        return new OrzTransaction(transactionManager, transactionDefinition);
    }

    public OrzTransaction newTransaction() {
        return getTransaction().propagationBehavior(PROPAGATION_REQUIRES_NEW);
    }

    public OrzTransactionExecutor executor() {
        return new OrzTransactionExecutor(newTransaction());
    }

    public OrzTransactionExecutor executor(OrzTransaction transaction) {
        return new OrzTransactionExecutor(transaction);
    }
}
