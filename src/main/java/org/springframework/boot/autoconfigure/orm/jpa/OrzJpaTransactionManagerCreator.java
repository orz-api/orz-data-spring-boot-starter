package org.springframework.boot.autoconfigure.orm.jpa;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import orz.springboot.data.source.OrzDataSourceAnnotation;

import javax.sql.DataSource;
import java.util.Collection;

@Slf4j
class OrzJpaTransactionManagerCreator {
    private final OrzDataSourceAnnotation annotation;
    private final DataSource dataSource;
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final ConfigurableListableBeanFactory beanFactory;
    private final ObjectProvider<JtaTransactionManager> jtaTransactionManager;
    private final ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders;
    private final ObjectProvider<SchemaManagementProvider> schemaManagementProviders;
    private final ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy;
    private final ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy;
    private final ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;
    private final ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers;

    OrzJpaTransactionManagerCreator(OrzDataSourceAnnotation annotation, DataSource dataSource, JpaProperties jpaProperties, HibernateProperties hibernateProperties, ConfigurableListableBeanFactory beanFactory, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders, ObjectProvider<SchemaManagementProvider> schemaManagementProviders, ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy, ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy, ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        this.annotation = annotation;
        this.dataSource = dataSource;
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
        this.beanFactory = beanFactory;
        this.jtaTransactionManager = jtaTransactionManager;
        this.metadataProviders = metadataProviders;
        this.schemaManagementProviders = schemaManagementProviders;
        this.physicalNamingStrategy = physicalNamingStrategy;
        this.implicitNamingStrategy = implicitNamingStrategy;
        this.hibernatePropertiesCustomizers = hibernatePropertiesCustomizers;
        this.transactionManagerCustomizers = transactionManagerCustomizers;
    }

    PlatformTransactionManager create() {
        var transactionManager = (JpaTransactionManager) new HibernateJpaConfiguration(
                dataSource,
                jpaProperties,
                beanFactory,
                jtaTransactionManager,
                hibernateProperties,
                metadataProviders,
                schemaManagementProviders,
                physicalNamingStrategy,
                implicitNamingStrategy,
                hibernatePropertiesCustomizers
        ).transactionManager(transactionManagerCustomizers);
        var config = annotation.getConfig().getTransaction();
        if (config.getDefaultTimeout() != null) {
            transactionManager.setDefaultTimeout((int) config.getDefaultTimeout().toSeconds());
        }
        if (config.getRollbackOnCommitFailure() != null) {
            transactionManager.setRollbackOnCommitFailure(config.getRollbackOnCommitFailure());
        }
        // TODO: name 用处不明
        transactionManager.setPersistenceUnitName(annotation.getName());
        return transactionManager;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getTransactionManagerBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getJpaTransactionManagerCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzJpaTransactionManagerCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getDataSourceBeanName())
                .addConstructorArgReference(annotation.getJpaPropertiesBeanName())
                .addConstructorArgReference(annotation.getHibernatePropertiesBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(PlatformTransactionManager.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
