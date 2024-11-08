package org.springframework.boot.autoconfigure.flyway;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import orz.springboot.base.misc.OrzBeanObjectProvider;
import orz.springboot.data.source.OrzDataSourceAnnotation;

import javax.sql.DataSource;

@Slf4j
class OrzFlywayCreator {
    private final OrzDataSourceAnnotation annotation;
    private final DataSource dataSource;
    private final FlywayProperties properties;
    private final FlywayConnectionDetails connectionDetails;
    private final ResourceProviderCustomizer resourceProviderCustomizer;
    private final ResourceLoader resourceLoader;
    private final ObjectProvider<FlywayConfigurationCustomizer> configurationCustomizers;
    private final ObjectProvider<JavaMigration> javaMigrations;
    private final ObjectProvider<Callback> callbacks;

    OrzFlywayCreator(OrzDataSourceAnnotation annotation, DataSource dataSource, FlywayProperties properties, FlywayConnectionDetails connectionDetails, ResourceProviderCustomizer resourceProviderCustomizer, ResourceLoader resourceLoader, ObjectProvider<FlywayConfigurationCustomizer> configurationCustomizers, ObjectProvider<JavaMigration> javaMigrations, ObjectProvider<Callback> callbacks) {
        this.annotation = annotation;
        this.dataSource = dataSource;
        this.properties = properties;
        this.connectionDetails = connectionDetails;
        this.resourceProviderCustomizer = resourceProviderCustomizer;
        this.resourceLoader = resourceLoader;
        this.configurationCustomizers = configurationCustomizers;
        this.javaMigrations = javaMigrations;
        this.callbacks = callbacks;
    }

    Flyway create() {
        // TODO: filter configurationCustomizers / javaMigrations / callbacks
        return new FlywayAutoConfiguration.FlywayConfiguration(properties).flyway(
                connectionDetails,
                resourceLoader,
                OrzBeanObjectProvider.of(dataSource),
                OrzBeanObjectProvider.empty(),
                configurationCustomizers,
                javaMigrations,
                callbacks,
                resourceProviderCustomizer
        );
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getFlywayBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getFlywayCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzFlywayCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getDataSourceBeanName())
                .addConstructorArgReference(annotation.getFlywayPropertiesBeanName())
                .addConstructorArgReference(annotation.getFlywayConnectionDetailsBeanName())
                .addConstructorArgReference(annotation.getFlywayResourceProviderCustomizerBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(Flyway.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
