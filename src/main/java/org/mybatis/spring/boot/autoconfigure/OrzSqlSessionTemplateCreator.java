package org.mybatis.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import orz.springboot.data.source.OrzDataSourceAnnotation;

import java.util.List;

@Slf4j
@SuppressWarnings("rawtypes")
class OrzSqlSessionTemplateCreator {
    private final OrzDataSourceAnnotation annotation;
    private final MybatisProperties properties;
    private final SqlSessionFactory sqlSessionFactory;
    private final ResourceLoader resourceLoader;
    private final ObjectProvider<Interceptor[]> interceptorsProvider;
    private final ObjectProvider<TypeHandler[]> typeHandlersProvider;
    private final ObjectProvider<LanguageDriver[]> languageDriversProvider;
    private final ObjectProvider<DatabaseIdProvider> databaseIdProvider;
    private final ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider;
    private final ObjectProvider<List<SqlSessionFactoryBeanCustomizer>> sqlSessionFactoryBeanCustomizers;

    OrzSqlSessionTemplateCreator(OrzDataSourceAnnotation annotation, MybatisProperties properties, SqlSessionFactory sqlSessionFactory, ResourceLoader resourceLoader, ObjectProvider<Interceptor[]> interceptorsProvider, ObjectProvider<TypeHandler[]> typeHandlersProvider, ObjectProvider<LanguageDriver[]> languageDriversProvider, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider, ObjectProvider<List<SqlSessionFactoryBeanCustomizer>> sqlSessionFactoryBeanCustomizers) {
        this.annotation = annotation;
        this.properties = properties;
        this.sqlSessionFactory = sqlSessionFactory;
        this.resourceLoader = resourceLoader;
        this.interceptorsProvider = interceptorsProvider;
        this.typeHandlersProvider = typeHandlersProvider;
        this.languageDriversProvider = languageDriversProvider;
        this.databaseIdProvider = databaseIdProvider;
        this.configurationCustomizersProvider = configurationCustomizersProvider;
        this.sqlSessionFactoryBeanCustomizers = sqlSessionFactoryBeanCustomizers;
    }

    SqlSessionTemplate create() {
        var configuration = new MybatisAutoConfiguration(
                properties,
                interceptorsProvider,
                typeHandlersProvider,
                languageDriversProvider,
                resourceLoader,
                databaseIdProvider,
                configurationCustomizersProvider,
                sqlSessionFactoryBeanCustomizers
        );
        configuration.afterPropertiesSet();
        return configuration.sqlSessionTemplate(sqlSessionFactory);
    }

    static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        var productBeanName = annotation.getSqlSessionTemplateBeanName();
        if (registry.containsBeanDefinition(productBeanName)) {
            log.info("Bean already registered: {}", productBeanName);
            return;
        }

        var creatorBeanName = annotation.getSqlSessionTemplateCreatorBeanName();
        registry.registerBeanDefinition(creatorBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzSqlSessionTemplateCreator.class)
                .addConstructorArgValue(annotation)
                .addConstructorArgReference(annotation.getMybatisPropertiesBeanName())
                .addConstructorArgReference(annotation.getSqlSessionFactoryBeanName())
                .setLazyInit(false)
                .getBeanDefinition());

        registry.registerBeanDefinition(productBeanName, BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class)
                .setFactoryMethodOnBean("create", creatorBeanName)
                .setLazyInit(false)
                .getBeanDefinition());
    }
}
