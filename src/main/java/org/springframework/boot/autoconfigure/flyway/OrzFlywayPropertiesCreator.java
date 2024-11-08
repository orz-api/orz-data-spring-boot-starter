package org.springframework.boot.autoconfigure.flyway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzFlywayPropertiesCreator {
    private final OrzDataSourceAnnotation annotation;
    private final Environment environment;

    OrzFlywayPropertiesCreator(OrzDataSourceAnnotation annotation, Environment environment) {
        this.annotation = annotation;
        this.environment = environment;
    }

    FlywayProperties create() {
        var properties = Binder.get(environment).bind("spring.flyway", FlywayProperties.class).orElseGet(FlywayProperties::new);
        var config = annotation.getConfig().getFlyway();
        if (!CollectionUtils.isEmpty(config.getLocations())) {
            properties.setLocations(config.getLocations());
        }
        // TODO: other flyway config
        return properties;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getFlywayPropertiesBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getFlywayPropertiesCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzFlywayPropertiesCreator.class)
                .addConstructorArgValue(annotation)
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(FlywayProperties.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
