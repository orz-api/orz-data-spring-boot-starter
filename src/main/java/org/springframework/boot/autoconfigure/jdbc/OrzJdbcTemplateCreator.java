package org.springframework.boot.autoconfigure.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import orz.springboot.data.source.OrzDataSourceAnnotation;

import javax.sql.DataSource;

@Slf4j
class OrzJdbcTemplateCreator {
    private final OrzDataSourceAnnotation annotation;
    private final DataSource dataSource;
    private final JdbcProperties properties;

    OrzJdbcTemplateCreator(OrzDataSourceAnnotation annotation, DataSource dataSource, JdbcProperties properties) {
        this.annotation = annotation;
        this.dataSource = dataSource;
        this.properties = properties;
    }

    JdbcTemplate create() {
        return new JdbcTemplateConfiguration().jdbcTemplate(dataSource, properties);
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getJdbcTemplateBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getJdbcTemplateCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzJdbcTemplateCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getDataSourceBeanName())
                .addConstructorArgReference(annotation.getJdbcPropertiesBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
