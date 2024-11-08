package org.springframework.boot.autoconfigure.flyway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;
import orz.springboot.data.source.OrzFlywayConnectionDetails;
import orz.springboot.data.source.OrzJdbcConnectionDetails;

@Slf4j
class OrzFlywayConnectionDetailsCreator {
    private final OrzDataSourceAnnotation annotation;
    private final OrzJdbcConnectionDetails jdbcConnectionDetails;

    OrzFlywayConnectionDetailsCreator(OrzDataSourceAnnotation annotation, OrzJdbcConnectionDetails jdbcConnectionDetails) {
        this.annotation = annotation;
        this.jdbcConnectionDetails = jdbcConnectionDetails;
    }

    FlywayConnectionDetails create() {
        return new OrzFlywayConnectionDetails(
                jdbcConnectionDetails.getJdbcUrl(),
                jdbcConnectionDetails.getUsername(),
                jdbcConnectionDetails.getPassword(),
                jdbcConnectionDetails.getDriverClassName()
        );
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getFlywayConnectionDetailsBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getFlywayConnectionDetailsCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzFlywayConnectionDetailsCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getJdbcConnectionDetailsBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(FlywayConnectionDetails.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
