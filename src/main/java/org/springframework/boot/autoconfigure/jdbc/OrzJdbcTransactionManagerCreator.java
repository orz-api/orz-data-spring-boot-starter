package org.springframework.boot.autoconfigure.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;
import orz.springboot.data.source.OrzDataSourceAnnotation;

import javax.sql.DataSource;

@Slf4j
class OrzJdbcTransactionManagerCreator {
    private final OrzDataSourceAnnotation annotation;
    private final DataSource dataSource;
    private final Environment environment;
    private final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers;

    OrzJdbcTransactionManagerCreator(OrzDataSourceAnnotation annotation, DataSource dataSource, Environment environment, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        this.annotation = annotation;
        this.dataSource = dataSource;
        this.environment = environment;
        // TODO: ???
        this.transactionManagerCustomizers = transactionManagerCustomizers;
    }

    PlatformTransactionManager create() {
        var transactionManager = new DataSourceTransactionManagerAutoConfiguration.JdbcTransactionManagerConfiguration().transactionManager(
                environment,
                dataSource,
                transactionManagerCustomizers
        );
        var config = annotation.getConfig().getTransaction();
        if (config.getDefaultTimeout() != null) {
            transactionManager.setDefaultTimeout((int) config.getDefaultTimeout().toSeconds());
        }
        if (config.getRollbackOnCommitFailure() != null) {
            transactionManager.setRollbackOnCommitFailure(config.getRollbackOnCommitFailure());
        }
        return transactionManager;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getTransactionManagerBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getJdbcTransactionManagerCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzJdbcTransactionManagerCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getDataSourceBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(PlatformTransactionManager.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
