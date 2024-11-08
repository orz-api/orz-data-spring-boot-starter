package org.springframework.boot.autoconfigure.orm.jpa;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.transaction.jta.JtaTransactionManager;
import orz.springboot.data.source.OrzDataSourceAnnotation;

import javax.sql.DataSource;
import java.util.Collection;

@Slf4j
class OrzEntityManagerFactoryCreator {
    private final OrzDataSourceAnnotation annotation;
    private final DataSource dataSource;
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final EntityManagerFactoryBuilder entityManagerFactoryBuilder;
    private final PersistenceManagedTypes persistenceManagedTypes;
    private final ConfigurableListableBeanFactory beanFactory;
    private final ObjectProvider<JtaTransactionManager> jtaTransactionManager;
    private final ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders;
    private final ObjectProvider<SchemaManagementProvider> schemaManagementProviders;
    private final ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy;
    private final ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy;
    private final ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    OrzEntityManagerFactoryCreator(OrzDataSourceAnnotation annotation, DataSource dataSource, JpaProperties jpaProperties, HibernateProperties hibernateProperties, EntityManagerFactoryBuilder entityManagerFactoryBuilder, PersistenceManagedTypes persistenceManagedTypes, ConfigurableListableBeanFactory beanFactory, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders, ObjectProvider<SchemaManagementProvider> schemaManagementProviders, ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy, ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy, ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        this.annotation = annotation;
        this.dataSource = dataSource;
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
        this.entityManagerFactoryBuilder = entityManagerFactoryBuilder;
        this.persistenceManagedTypes = persistenceManagedTypes;
        this.beanFactory = beanFactory;
        this.jtaTransactionManager = jtaTransactionManager;
        this.metadataProviders = metadataProviders;
        this.schemaManagementProviders = schemaManagementProviders;
        this.physicalNamingStrategy = physicalNamingStrategy;
        this.implicitNamingStrategy = implicitNamingStrategy;
        this.hibernatePropertiesCustomizers = hibernatePropertiesCustomizers;
    }

    LocalContainerEntityManagerFactoryBean create() {
        var entityManagerFactory = new HibernateJpaConfiguration(
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
        ).entityManagerFactory(
                entityManagerFactoryBuilder,
                persistenceManagedTypes
        );
        // TODO: name 用处不明
        entityManagerFactory.setPersistenceUnitName(annotation.getName());
        return entityManagerFactory;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getEntityManagerFactoryBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getEntityManagerFactoryCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzEntityManagerFactoryCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getDataSourceBeanName())
                .addConstructorArgReference(annotation.getJpaPropertiesBeanName())
                .addConstructorArgReference(annotation.getHibernatePropertiesBeanName())
                .addConstructorArgReference(annotation.getEntityManagerFactoryBuilderBeanName())
                .addConstructorArgReference(annotation.getPersistenceManagedTypesBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(LocalContainerEntityManagerFactoryBean.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
