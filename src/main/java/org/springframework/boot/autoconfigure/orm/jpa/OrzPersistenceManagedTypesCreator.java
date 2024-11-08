package org.springframework.boot.autoconfigure.orm.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.persistenceunit.ManagedClassNameFilter;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import orz.springboot.data.source.OrzDataSourceAnnotation;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

@Slf4j
class OrzPersistenceManagedTypesCreator {
    private final OrzDataSourceAnnotation annotation;
    private final ResourceLoader resourceLoader;
    private final ObjectProvider<ManagedClassNameFilter> managedClassNameFilter;

    OrzPersistenceManagedTypesCreator(OrzDataSourceAnnotation annotation, ResourceLoader resourceLoader, ObjectProvider<ManagedClassNameFilter> managedClassNameFilter) {
        this.annotation = annotation;
        this.resourceLoader = resourceLoader;
        this.managedClassNameFilter = managedClassNameFilter;
    }

    PersistenceManagedTypes create() {
        var packages = new LinkedHashSet<String>();
        Arrays.stream(annotation.getAttributes().getClassArray("entityClasses"))
                .forEach(clazz -> packages.add(clazz.getPackage().getName()));
        Collections.addAll(packages, annotation.getAttributes().getStringArray("entityPackages"));
        if (packages.isEmpty()) {
            throw new FatalBeanException("@OrzDataSource must specify entityClasses or entityPackages");
        }
        return new PersistenceManagedTypesScanner(resourceLoader, managedClassNameFilter.getIfAvailable())
                .scan(packages.toArray(new String[0]));
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getPersistenceManagedTypesBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getPersistenceManagedTypesCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzPersistenceManagedTypesCreator.class)
                .addConstructorArgValue(annotation)
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(PersistenceManagedTypes.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
