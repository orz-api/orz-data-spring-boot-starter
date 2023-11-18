package orz.springboot.data.transaction;


import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

public class OrzTransaction implements TransactionDefinition {
    private final PlatformTransactionManager transactionManager;
    private final TransactionDefinition defaultDefinition;
    private Integer propagationBehavior;
    private Integer isolationLevel;
    private Integer timeout;
    private Boolean readOnly;
    private String name;
    private TransactionStatus transactionStatus;

    OrzTransaction(PlatformTransactionManager transactionManager, TransactionDefinition defaultDefinition) {
        this.transactionManager = transactionManager;
        this.defaultDefinition = defaultDefinition;
    }

    public synchronized void begin() {
        if (transactionStatus != null) {
            throw new IllegalStateException("transaction already started");
        }
        transactionStatus = transactionManager.getTransaction(this);
    }

    public void commit() {
        if (transactionStatus != null && !transactionStatus.isCompleted()) {
            transactionManager.commit(transactionStatus);
        }
    }

    public void rollback() {
        if (transactionStatus != null && !transactionStatus.isCompleted()) {
            transactionManager.rollback(transactionStatus);
        }
    }

    public OrzTransaction propagationBehavior(int propagationBehavior) {
        if (transactionStatus != null) {
            throw new IllegalStateException("transaction already started");
        }
        this.propagationBehavior = propagationBehavior;
        return this;
    }

    public OrzTransaction isolationLevel(int isolationLevel) {
        if (transactionStatus != null) {
            throw new IllegalStateException("transaction already started");
        }
        this.isolationLevel = isolationLevel;
        return this;
    }

    public OrzTransaction timeout(int timeout) {
        if (transactionStatus != null) {
            throw new IllegalStateException("transaction already started");
        }
        this.timeout = timeout;
        return this;
    }

    public OrzTransaction readOnly(boolean readOnly) {
        if (transactionStatus != null) {
            throw new IllegalStateException("transaction already started");
        }
        this.readOnly = readOnly;
        return this;
    }

    public OrzTransaction name(String name) {
        if (transactionStatus != null) {
            throw new IllegalStateException("transaction already started");
        }
        this.name = name;
        return this;
    }

    @Override
    public int getPropagationBehavior() {
        return ObjectUtils.defaultIfNull(propagationBehavior, defaultDefinition.getPropagationBehavior());
    }

    @Override
    public int getIsolationLevel() {
        return ObjectUtils.defaultIfNull(isolationLevel, defaultDefinition.getIsolationLevel());
    }

    @Override
    public int getTimeout() {
        return ObjectUtils.defaultIfNull(timeout, defaultDefinition.getTimeout());
    }

    @Override
    public boolean isReadOnly() {
        return ObjectUtils.defaultIfNull(readOnly, defaultDefinition.isReadOnly());
    }

    @Override
    public String getName() {
        return ObjectUtils.defaultIfNull(name, defaultDefinition.getName());
    }
}
