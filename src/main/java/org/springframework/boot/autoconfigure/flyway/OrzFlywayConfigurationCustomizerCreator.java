package org.springframework.boot.autoconfigure.flyway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzFlywayConfigurationCustomizerCreator {
    private final OrzDataSourceAnnotation annotation;
    private final FlywayProperties properties;

    OrzFlywayConfigurationCustomizerCreator(OrzDataSourceAnnotation annotation, FlywayProperties properties) {
        this.annotation = annotation;
        this.properties = properties;
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        // TODO: Implement this method
        // FlywayAutoConfiguration.SqlServerFlywayConfigurationCustomizer
        // FlywayAutoConfiguration.OracleFlywayConfigurationCustomizer
        // FlywayAutoConfiguration.PostgresqlFlywayConfigurationCustomizer
    }
}
