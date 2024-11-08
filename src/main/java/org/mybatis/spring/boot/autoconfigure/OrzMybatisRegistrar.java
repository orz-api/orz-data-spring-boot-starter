package org.mybatis.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import orz.springboot.data.source.OrzDataSourceAnnotation;

@Slf4j
public class OrzMybatisRegistrar {
    public static void registerBeanDefinitions(Environment environment, BeanDefinitionRegistry registry, OrzDataSourceAnnotation annotation) {
        // TODO: check class exists

        var enable = annotation.getAttributes().getBoolean("mybatis");
        if (!enable) {
            log.info("Mybatis is disabled for datasource: {}", annotation.getName());
            return;
        }

        OrzMybatisPropertiesCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzSqlSessionFactoryCreator.registerBeanDefinitions(environment, registry, annotation);
        OrzSqlSessionTemplateCreator.registerBeanDefinitions(environment, registry, annotation);
    }
}
