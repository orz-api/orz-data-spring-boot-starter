package org.springframework.boot.autoconfigure.flyway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzFlywayResourceProviderCustomizerCreator {
    private final OrzDataSourceAnnotation annotation;
    private final FlywayProperties properties;

    OrzFlywayResourceProviderCustomizerCreator(OrzDataSourceAnnotation annotation, FlywayProperties properties) {
        this.annotation = annotation;
        this.properties = properties;
    }

    ResourceProviderCustomizer create() {
        return new FlywayAutoConfiguration.FlywayConfiguration(properties).resourceProviderCustomizer();
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getFlywayResourceProviderCustomizerBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            return;
        }

        var creatorBeanName = annotation.getFlywayResourceProviderCustomizerCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzFlywayResourceProviderCustomizerCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getFlywayPropertiesBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(ResourceProviderCustomizer.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
