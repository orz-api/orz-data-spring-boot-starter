package org.springframework.boot.autoconfigure.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;
import orz.springboot.data.source.OrzJdbcConnectionDetails;

@Slf4j
class OrzJdbcConnectionDetailsCreator {
    private final OrzDataSourceAnnotation annotation;
    private final DataSourceProperties properties;

    public OrzJdbcConnectionDetailsCreator(OrzDataSourceAnnotation annotation, DataSourceProperties properties) {
        this.annotation = annotation;
        this.properties = properties;
    }

    JdbcConnectionDetails create() {
        return new OrzJdbcConnectionDetails(
                properties.determineUrl(),
                properties.determineUsername(),
                properties.determinePassword(),
                properties.determineDriverClassName()
        );
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getJdbcConnectionDetailsBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getJdbcConnectionDetailsCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzJdbcConnectionDetailsCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getDataSourcePropertiesBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(JdbcConnectionDetails.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
