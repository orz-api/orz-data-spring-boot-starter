package orz.springboot.data;

import lombok.SneakyThrows;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import orz.springboot.base.OrzBaseUtils;

public class OrzTransaction {
    private final PlatformTransactionManager manager;
    private final DefaultTransactionDefinition definition;
    private OrzLock lock;

    public OrzTransaction(PlatformTransactionManager manager, OrzLock lock) {
        this.manager = manager;
        this.definition = new DefaultTransactionDefinition();
        this.lock = lock;
    }

    public static OrzTransaction of(String manager) {
        return OrzTransaction.of(manager, null);
    }

    public static OrzTransaction of(String manager, OrzLock lock) {
        var beanName = manager;
        if (!beanName.endsWith("TransactionManager")) {
            beanName = beanName + "TransactionManager";
        }
        var managerBean = OrzBaseUtils.getAppContext().getBean(beanName, PlatformTransactionManager.class);
        return OrzTransaction.of(managerBean, lock);
    }

    public static OrzTransaction of(PlatformTransactionManager manager) {
        return OrzTransaction.of(manager, null);
    }

    public static OrzTransaction of(PlatformTransactionManager manager, OrzLock lock) {
        return new OrzTransaction(manager, lock);
    }

    public OrzTransaction propagationRequired() {
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return this;
    }

    public OrzTransaction propagationRequiresNew() {
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return this;
    }

    public OrzTransaction propagationMandatory() {
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
        return this;
    }

    public OrzTransaction propagationSupports() {
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        return this;
    }

    public OrzTransaction propagationNotSupported() {
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        return this;
    }

    public OrzTransaction propagationNever() {
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
        return this;
    }

    public OrzTransaction propagationNested() {
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
        return this;
    }

    public OrzTransaction isolationDefault() {
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        return this;
    }

    public OrzTransaction isolationReadUncommitted() {
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        return this;
    }

    public OrzTransaction isolationReadCommitted() {
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return this;
    }

    public OrzTransaction isolationRepeatableRead() {
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        return this;
    }

    public OrzTransaction isolationSerializable() {
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        return this;
    }

    public OrzTransaction timeout(int timeout) {
        definition.setTimeout(timeout);
        return this;
    }

    public OrzTransaction readOnly(boolean readOnly) {
        definition.setReadOnly(readOnly);
        return this;
    }

    public OrzTransaction name(String name) {
        definition.setName(name);
        return this;
    }

    public OrzTransaction lock(OrzLock lock) {
        this.lock = lock;
        return this;
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

    public <P, R, E extends Exception> R exec(Action4<P, R, E> action, P param) throws E {
        if (lock != null) {
            return lock.exec(() -> doExec(action, param));
        } else {
            return doExec(action, param);
        }
    }

    @SneakyThrows
    private <P, R, E extends Exception> R doExec(Action4<P, R, E> action, P param) throws E {
        var status = (TransactionStatus) null;
        try {
            status = manager.getTransaction(definition);
            return action.run(param);
        } catch (OrzTransactionException e) {
            if (e.isRollback()) {
                manager.rollback(status);
            }
            throw e.getException();
        } catch (Exception e) {
            if (status != null) {
                manager.rollback(status);
            }
            throw e;
        } finally {
            if (status != null) {
                manager.commit(status);
            }
        }
    }

    public interface Action1<E extends Exception> {
        void run() throws OrzTransactionException, E;
    }

    public interface Action2<R, E extends Exception> {
        R run() throws OrzTransactionException, E;
    }

    public interface Action3<P, E extends Exception> {
        void run(P param) throws OrzTransactionException, E;
    }

    public interface Action4<P, R, E extends Exception> {
        R run(P param) throws OrzTransactionException, E;
    }
}
