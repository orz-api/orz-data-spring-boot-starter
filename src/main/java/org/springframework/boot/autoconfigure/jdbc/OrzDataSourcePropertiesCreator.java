package org.springframework.boot.autoconfigure.jdbc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzDataSourcePropertiesCreator {
    private final OrzDataSourceAnnotation annotation;
    private final Environment environment;

    OrzDataSourcePropertiesCreator(OrzDataSourceAnnotation annotation, Environment environment) {
        this.annotation = annotation;
        this.environment = environment;
    }

    @SneakyThrows
    DataSourceProperties create() {
        var config = annotation.getConfig();
        var properties = Binder.get(environment).bind("spring.datasource", DataSourceProperties.class).orElseGet(DataSourceProperties::new);
        if (StringUtils.isNotBlank(config.getUrl())) {
            properties.setUrl(config.getUrl());
        }
        if (StringUtils.isNotBlank(config.getUsername())) {
            properties.setUsername(config.getUsername());
        }
        if (StringUtils.isNotBlank(config.getPassword())) {
            properties.setPassword(config.getPassword());
        }
        if (StringUtils.isNotBlank(config.getDriver())) {
            properties.setDriverClassName(config.getDriver());
        }
        properties.setBeanClassLoader(getClass().getClassLoader());
        // TODO: other datasource config
        // TODO: naming
        // properties.setName(name);
        properties.afterPropertiesSet();
        return properties;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getDataSourcePropertiesBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getDataSourcePropertiesCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzDataSourcePropertiesCreator.class)
                .addConstructorArgValue(annotation)
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(DataSourceProperties.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
