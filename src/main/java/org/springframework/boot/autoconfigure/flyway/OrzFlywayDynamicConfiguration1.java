package org.springframework.boot.autoconfigure.flyway;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.ResourceLoader;
import orz.springboot.data.OrzDataProps;
import orz.springboot.data.flyway.OrzFlywayConnectionDetails;
import orz.springboot.data.jdbc.OrzJdbcConnectionDetails;

import javax.sql.DataSource;

public class OrzFlywayDynamicConfiguration1 {
    private final String name;
    private final OrzDataProps props;
    private final ResourceLoader resourceLoader;
    private final ObjectProvider<DataSource> dataSource;
    private final ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers;
    private final ObjectProvider<JavaMigration> javaMigrations;
    private final ObjectProvider<Callback> callbacks;
    private final ObjectProvider<FlywayMigrationStrategy> migrationStrategy;

    public OrzFlywayDynamicConfiguration1(String name, OrzDataProps props, ResourceLoader resourceLoader, ObjectProvider<DataSource> dataSource, ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers, ObjectProvider<JavaMigration> javaMigrations, ObjectProvider<Callback> callbacks, ObjectProvider<FlywayMigrationStrategy> migrationStrategy, FlywayAutoConfiguration.FlywayConfiguration flywayConfiguration) {
        this.name = name;
        this.props = props;
        this.resourceLoader = resourceLoader;
        this.dataSource = dataSource;
        this.fluentConfigurationCustomizers = fluentConfigurationCustomizers;
        this.javaMigrations = javaMigrations;
        this.callbacks = callbacks;
        this.migrationStrategy = migrationStrategy;
    }

    public OrzFlywayConnectionDetails createFlywayConnectionDetails(OrzJdbcConnectionDetails details) {
        return new OrzFlywayConnectionDetails(
                details.getUsername(),
                details.getPassword(),
                details.getJdbcUrl(),
                details.getDriverClassName()
        );
    }

    /*
    TODO: Implement the following methods:
    @Bean
    ResourceProviderCustomizer resourceProviderCustomizer() {
        return new ResourceProviderCustomizer();
    }

    @Bean
    @ConditionalOnClass(name = "org.flywaydb.database.sqlserver.SQLServerConfigurationExtension")
    FlywayAutoConfiguration.SqlServerFlywayConfigurationCustomizer sqlServerFlywayConfigurationCustomizer() {
        return new FlywayAutoConfiguration.SqlServerFlywayConfigurationCustomizer(this.properties);
    }

    @Bean
    @ConditionalOnClass(name = "org.flywaydb.database.oracle.OracleConfigurationExtension")
    FlywayAutoConfiguration.OracleFlywayConfigurationCustomizer oracleFlywayConfigurationCustomizer() {
        return new FlywayAutoConfiguration.OracleFlywayConfigurationCustomizer(this.properties);
    }

    @Bean
    @ConditionalOnClass(name = "org.flywaydb.database.postgresql.PostgreSQLConfigurationExtension")
    FlywayAutoConfiguration.PostgresqlFlywayConfigurationCustomizer postgresqlFlywayConfigurationCustomizer() {
        return new FlywayAutoConfiguration.PostgresqlFlywayConfigurationCustomizer(this.properties);
    }
    */
}
