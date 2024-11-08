package org.springframework.boot.autoconfigure.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzJdbcPropertiesCreator {
    private final OrzDataSourceAnnotation annotation;
    private final Environment environment;

    OrzJdbcPropertiesCreator(OrzDataSourceAnnotation annotation, Environment environment) {
        this.annotation = annotation;
        this.environment = environment;
    }

    JdbcProperties create() {
        var properties = Binder.get(environment).bind("spring.jdbc", JdbcProperties.class).orElseGet(JdbcProperties::new);
        var config = annotation.getConfig().getJdbc();
        // TODO: jdbc config
        return properties;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getJdbcPropertiesBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getJdbcPropertiesCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzJdbcPropertiesCreator.class)
                .addConstructorArgValue(annotation)
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(JdbcProperties.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
