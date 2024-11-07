package org.springframework.boot.autoconfigure.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.ClassUtils;
import orz.springboot.data.OrzDataProps;
import orz.springboot.data.OrzDataProps.SourceConfig;
import orz.springboot.data.OrzDataUtils;
import orz.springboot.data.jdbc.OrzJdbcConnectionDetails;

@Slf4j
public class OrzJdbcDynamicConfiguration {
    private final DataSourceProperties dataSourceProperties;

    @SneakyThrows
    public OrzJdbcDynamicConfiguration(String name, OrzDataProps props) {
        var config = props.getSource().get(name);
        this.dataSourceProperties = new DataSourceProperties();
        this.dataSourceProperties.setUrl(config.getUrl());
        this.dataSourceProperties.setUsername(config.getUsername());
        this.dataSourceProperties.setPassword(config.getPassword());
        this.dataSourceProperties.setDriverClassName(config.getDriver());
        this.dataSourceProperties.setBeanClassLoader(getClass().getClassLoader());
        // TODO: naming
        // this.properties.setName(name);
        this.dataSourceProperties.afterPropertiesSet();
    }

    public OrzJdbcConnectionDetails createJdbcConnectionDetails() {
        return new OrzJdbcConnectionDetails(
                dataSourceProperties.determineUrl(),
                dataSourceProperties.determineUsername(),
                dataSourceProperties.determinePassword(),
                dataSourceProperties.determineDriverClassName()
        );
    }

    public HikariDataSource createHikariDataSource(OrzJdbcConnectionDetails connectionDetails) {
        var dataSource = new DataSourceConfiguration.Hikari().dataSource(dataSourceProperties, connectionDetails);
        // TODO: hikari config
        return dataSource;
    }

    public static void registerBeanDefinitions1(BeanDefinitionRegistry registry, String source, SourceConfig config) {
        log.debug("Registering bean definitions for {}", source);

        var configurationBeanName = OrzDataUtils.getJdbcDynamicConfigurationBeanName(source);
        registry.registerBeanDefinition(configurationBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzJdbcDynamicConfiguration.class)
                .addConstructorArgValue(source)
                .addConstructorArgReference(OrzDataProps.class.getName())
                .setLazyInit(false)
                .getBeanDefinition());

        if (!ClassUtils.isPresent("com.zaxxer.hikari.HikariDataSource", OrzJdbcDynamicConfiguration.class.getClassLoader())) {
            // TODO: only support HikariCP
            throw new IllegalStateException("HikariCP is required for dynamic data source configuration");
        }
        var dataSourceBeanName = OrzDataUtils.getDataSourceBeanName(source);
        registry.registerBeanDefinition(dataSourceBeanName, BeanDefinitionBuilder.genericBeanDefinition(HikariDataSource.class)
                .setFactoryMethodOnBean("createHikariDataSource", configurationBeanName)
                .addConstructorArgReference(OrzDataUtils.getJdbcConnectionDetailsBeanName(source))
                .setLazyInit(false)
                .getBeanDefinition());
    }

    public static void registerBeanDefinitions2(BeanDefinitionRegistry registry, String source, SourceConfig config) {
        var jdbcConnectionDetailsBeanName = OrzDataUtils.getJdbcConnectionDetailsBeanName(source);
        if (!registry.containsBeanDefinition(jdbcConnectionDetailsBeanName)) {
            var configurationBeanName = OrzDataUtils.getJdbcDynamicConfigurationBeanName(source);
            registry.registerBeanDefinition(jdbcConnectionDetailsBeanName, BeanDefinitionBuilder.genericBeanDefinition(OrzJdbcConnectionDetails.class)
                    .setFactoryMethodOnBean("createJdbcConnectionDetails", configurationBeanName)
                    .setLazyInit(false)
                    .getBeanDefinition());
        } else {
            log.debug("Bean already registered: {}", jdbcConnectionDetailsBeanName);
        }
    }
}
