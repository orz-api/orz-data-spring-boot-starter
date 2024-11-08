package org.springframework.boot.autoconfigure.orm.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzHibernatePropertiesCreator {
    private final OrzDataSourceAnnotation annotation;
    private final Environment environment;

    OrzHibernatePropertiesCreator(OrzDataSourceAnnotation annotation, Environment environment) {
        this.annotation = annotation;
        this.environment = environment;
    }

    HibernateProperties create() {
        var properties = Binder.get(environment).bind("spring.jpa.hibernate", HibernateProperties.class).orElseGet(HibernateProperties::new);
        var config = annotation.getConfig().getJpa().getHibernate();
        // TODO: hibernate config
        return properties;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getHibernatePropertiesBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getHibernatePropertiesCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzHibernatePropertiesCreator.class)
                .addConstructorArgValue(annotation)
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(HibernateProperties.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
