package org.springframework.boot.autoconfigure.orm.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
public class OrzJpaRegistrar {
    public static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        // TODO: check class exists

        var enable = annotation.getAttributes().getBoolean("jpa");
        if (!enable) {
            log.info("JPA is disabled for datasource: {}", annotation.getName());
            return;
        }

        OrzJpaPropertiesCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzHibernatePropertiesCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzJpaVendorAdapterCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzPersistenceManagedTypesCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzEntityManagerFactoryBuilderCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzEntityManagerFactoryCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzJpaTransactionManagerCreator.registerBeanDefinitions(environment, registry, annotation);
    }
}
