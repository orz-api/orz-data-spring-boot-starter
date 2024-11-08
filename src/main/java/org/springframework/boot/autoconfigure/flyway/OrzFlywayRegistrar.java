package org.springframework.boot.autoconfigure.flyway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
public class OrzFlywayRegistrar {
    public static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        if (!ClassUtils.isPresent("org.flywaydb.core.Flyway", OrzFlywayRegistrar.class.getClassLoader())) {
            log.info("org.flywaydb.core.Flyway not present, skipping bean registration");
            return;
        }

        var enabled = environment.getProperty("spring.flyway.enabled", Boolean.class, true);
        if (!enabled) {
            log.info("spring.flyway.enabled is false, skipping bean registration");
            return;
        }

        if (Boolean.FALSE.equals(annotation.getConfig().getFlyway().getEnabled())) {
            log.info("Flyway is disabled for datasource: {}", annotation.getName());
            return;
        }

        OrzFlywayPropertiesCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzFlywayConnectionDetailsCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzFlywayResourceProviderCustomizerCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzFlywayConfigurationCustomizerCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzFlywayCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzFlywayMigrationInitializerCreator.registerBeanDefinitions(environment, registry, annotation);
    }
}
