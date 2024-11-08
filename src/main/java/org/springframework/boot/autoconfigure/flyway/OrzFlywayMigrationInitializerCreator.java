package org.springframework.boot.autoconfigure.flyway;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzFlywayMigrationInitializerCreator {
    private final OrzDataSourceAnnotation annotation;
    private final FlywayProperties properties;
    private final Flyway flyway;
    private final ObjectProvider<FlywayMigrationStrategy> migrationStrategy;

    OrzFlywayMigrationInitializerCreator(OrzDataSourceAnnotation annotation, FlywayProperties properties, Flyway flyway, ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
        this.annotation = annotation;
        this.properties = properties;
        this.flyway = flyway;
        this.migrationStrategy = migrationStrategy;
    }

    FlywayMigrationInitializer create() {
        // TODO: filter migrationStrategy
        return new FlywayAutoConfiguration.FlywayConfiguration(properties).flywayInitializer(flyway, migrationStrategy);
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getFlywayMigrationInitializerBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getFlywayMigrationInitializerCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzFlywayMigrationInitializerCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getFlywayPropertiesBeanName())
                .addConstructorArgReference(annotation.getFlywayBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(FlywayMigrationInitializer.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
