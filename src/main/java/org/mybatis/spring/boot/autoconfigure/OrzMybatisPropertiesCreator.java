package org.mybatis.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzMybatisPropertiesCreator {
    private final OrzDataSourceAnnotation annotation;
    private final Environment environment;

    OrzMybatisPropertiesCreator(OrzDataSourceAnnotation annotation, Environment environment) {
        this.annotation = annotation;
        this.environment = environment;
    }

    MybatisProperties create() {
        var properties = Binder.get(environment).bind(MybatisProperties.MYBATIS_PREFIX, MybatisProperties.class).orElseGet(MybatisProperties::new);
        var config = annotation.getConfig().getMybatis();
        // TODO: mybatis config
        return properties;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getMybatisPropertiesBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getMybatisPropertiesCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzMybatisPropertiesCreator.class)
                .addConstructorArgValue(annotation)
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(MybatisProperties.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
