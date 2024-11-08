package org.springframework.boot.autoconfigure.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
public class OrzJdbcRegistrar {
    public static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        OrzDataSourcePropertiesCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzHikariDataSourceCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzJdbcPropertiesCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzJdbcTemplateCreator.registerBeanDefinitions(environment, registry, annotation);
    }

    public static void registerConnectionDetailsBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        OrzJdbcConnectionDetailsCreator.registerBeanDefinitions(environment, registry, annotation);
    }

    public static void registerTransactionBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        OrzJdbcTransactionManagerCreator.registerBeanDefinitions(environment, registry, annotation);
    }
}
