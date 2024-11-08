package org.springframework.boot.autoconfigure.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import orz.springboot.data.source.OrzDataSourceAnnotation;
import orz.springboot.data.source.OrzJdbcConnectionDetails;

import javax.sql.DataSource;

@Slf4j
class OrzHikariDataSourceCreator {
    private final OrzDataSourceAnnotation annotation;
    private final DataSourceProperties properties;
    private final OrzJdbcConnectionDetails connectionDetails;

    OrzHikariDataSourceCreator(OrzDataSourceAnnotation annotation, DataSourceProperties properties, OrzJdbcConnectionDetails connectionDetails) {
        this.annotation = annotation;
        this.properties = properties;
        this.connectionDetails = connectionDetails;
    }

    DataSource create() {
        var dataSource = new DataSourceConfiguration.Hikari().dataSource(properties, connectionDetails);
        // TODO: datasource config
        return dataSource;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getDataSourceBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        if (!ClassUtils.isPresent("com.zaxxer.hikari.HikariDataSource", OrzHikariDataSourceCreator.class.getClassLoader())) {
            log.info("com.zaxxer.hikari.HikariDataSource not present, skipping bean registration: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getHikariDataSourceCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzHikariDataSourceCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getDataSourcePropertiesBeanName())
                .addConstructorArgReference(annotation.getJdbcConnectionDetailsBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(DataSource.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
