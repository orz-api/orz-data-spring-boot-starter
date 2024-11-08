package org.springframework.boot.autoconfigure.transaction;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

public class OrzTransactionRegistrar {
    public static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        OrzTransactionTemplateCreator.registerBeanDefinitions(environment, registry, annotation);
    }
}
