package orz.springboot.data.source;

import lombok.Data;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import orz.springboot.data.OrzDataProps.SourceConfig;

@Data
public class OrzDataSourceAnnotation {
    private final String name;
    private final AnnotationMetadata metadata;
    private final AnnotationAttributes attributes;
    private final SourceConfig config;

    // region jdbc bean names

    public String getDataSourcePropertiesBeanName() {
        return name + "DataSourceProperties";
    }

    public String getDataSourcePropertiesCreatorBeanName() {
        return name + "DataSourcePropertiesCreator";
    }

    public String getDataSourceBeanName() {
        return name + "DataSource";
    }

    public String getHikariDataSourceCreatorBeanName() {
        return name + "HikariDataSourceCreator";
    }

    public String getJdbcConnectionDetailsBeanName() {
        return name + "JdbcConnectionDetails";
    }

    public String getJdbcConnectionDetailsCreatorBeanName() {
        return name + "JdbcConnectionDetailsCreator";
    }

    public String getJdbcPropertiesBeanName() {
        return name + "JdbcProperties";
    }

    public String getJdbcPropertiesCreatorBeanName() {
        return name + "JdbcPropertiesCreator";
    }

    public String getJdbcTemplateBeanName() {
        return name + "JdbcTemplate";
    }

    public String getJdbcTemplateCreatorBeanName() {
        return name + "JdbcTemplateCreator";
    }

    public String getJdbcTransactionManagerCreatorBeanName() {
        return name + "JdbcTransactionManagerCreator";
    }

    // endregion

    // region flyway bean names

    public String getFlywayConnectionDetailsBeanName() {
        return name + "FlywayConnectionDetails";
    }

    public String getFlywayConnectionDetailsCreatorBeanName() {
        return name + "FlywayConnectionDetailsCreator";
    }

    public String getFlywayPropertiesBeanName() {
        return name + "FlywayProperties";
    }

    public String getFlywayPropertiesCreatorBeanName() {
        return name + "FlywayPropertiesCreator";
    }

    public String getFlywayResourceProviderCustomizerBeanName() {
        return name + "FlywayResourceProviderCustomizer";
    }

    public String getFlywayResourceProviderCustomizerCreatorBeanName() {
        return name + "FlywayResourceProviderCustomizerCreator";
    }

    public String getFlywayBeanName() {
        return name + "Flyway";
    }

    public String getFlywayCreatorBeanName() {
        return name + "FlywayCreator";
    }

    public String getFlywayMigrationInitializerBeanName() {
        return name + "FlywayMigrationInitializer";
    }

    public String getFlywayMigrationInitializerCreatorBeanName() {
        return name + "FlywayMigrationInitializerCreator";
    }

    // endregion

    // region jpa bean names

    public String getJpaPropertiesBeanName() {
        return name + "JpaProperties";
    }

    public String getJpaPropertiesCreatorBeanName() {
        return name + "JpaPropertiesCreator";
    }

    public String getHibernatePropertiesBeanName() {
        return name + "HibernateProperties";
    }

    public String getHibernatePropertiesCreatorBeanName() {
        return name + "HibernatePropertiesCreator";
    }

    public String getJpaVendorAdapterBeanName() {
        return name + "JpaVendorAdapter";
    }

    public String getJpaVendorAdapterCreatorBeanName() {
        return name + "JpaVendorAdapterCreator";
    }

    public String getEntityManagerFactoryBuilderBeanName() {
        return name + "EntityManagerFactoryBuilder";
    }

    public String getEntityManagerFactoryBuilderCreatorBeanName() {
        return name + "EntityManagerFactoryBuilderCreator";
    }

    public String getPersistenceManagedTypesBeanName() {
        return name + "PersistenceManagedTypes";
    }

    public String getPersistenceManagedTypesCreatorBeanName() {
        return name + "PersistenceManagedTypesCreator";
    }

    public String getEntityManagerFactoryBeanName() {
        return name + "EntityManagerFactory";
    }

    public String getEntityManagerFactoryCreatorBeanName() {
        return name + "EntityManagerFactoryCreator";
    }

    public String getJpaTransactionManagerCreatorBeanName() {
        return name + "JpaTransactionManagerCreator";
    }

    // endregion

    // region transaction bean names

    public String getTransactionManagerBeanName() {
        return name + "TransactionManager";
    }

    public String getTransactionTemplateBeanName() {
        return name + "TransactionTemplate";
    }

    public String getTransactionTemplateCreatorBeanName() {
        return name + "TransactionTemplateCreator";
    }

    // endregion

    // region mybatis bean names

    public String getMybatisPropertiesBeanName() {
        return name + "MybatisProperties";
    }

    public String getMybatisPropertiesCreatorBeanName() {
        return name + "MybatisPropertiesCreator";
    }

    public String getSqlSessionFactoryBeanName() {
        return name + "SqlSessionFactory";
    }

    public String getSqlSessionFactoryCreatorBeanName() {
        return name + "SqlSessionFactoryCreator";
    }

    public String getSqlSessionTemplateBeanName() {
        return name + "SqlSessionTemplate";
    }

    public String getSqlSessionTemplateCreatorBeanName() {
        return name + "SqlSessionTemplateCreator";
    }

    // endregion
}
