package org.springframework.boot.autoconfigure.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
class OrzTransactionTemplateCreator {
    private final OrzDataSourceAnnotation annotation;
    private final PlatformTransactionManager transactionManager;

    OrzTransactionTemplateCreator(OrzDataSourceAnnotation annotation, PlatformTransactionManager transactionManager) {
        this.annotation = annotation;
        this.transactionManager = transactionManager;
    }

    TransactionTemplate create() {
        return new TransactionAutoConfiguration.TransactionTemplateConfiguration().transactionTemplate(transactionManager);
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getTransactionTemplateBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getTransactionTemplateCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzTransactionTemplateCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getTransactionManagerBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(TransactionTemplate.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
