package org.springframework.boot.autoconfigure.flyway;

import jakarta.annotation.Nonnull;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.ResourceLoader;
import orz.springboot.data.OrzDataProps;
import orz.springboot.data.flyway.OrzFlywayConnectionDetails;

import javax.sql.DataSource;

public class OrzFlywayDynamicConfiguration2 {
    private final String name;
    private final OrzDataProps props;
    private final OrzFlywayConnectionDetails connectionDetails;
    private final ResourceLoader resourceLoader;
    private final ObjectProvider<DataSource> dataSource;
    private final ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers;
    private final ObjectProvider<JavaMigration> javaMigrations;
    private final ObjectProvider<Callback> callbacks;
    private final ResourceProviderCustomizer resourceProviderCustomizer;
    private final ObjectProvider<FlywayMigrationStrategy> migrationStrategy;

    public OrzFlywayDynamicConfiguration2(String name, OrzDataProps props, OrzFlywayConnectionDetails connectionDetails, ResourceLoader resourceLoader, ObjectProvider<DataSource> dataSource, ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers, ObjectProvider<JavaMigration> javaMigrations, ObjectProvider<Callback> callbacks, ResourceProviderCustomizer resourceProviderCustomizer, ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
        this.name = name;
        this.props = props;
        this.connectionDetails = connectionDetails;
        this.resourceLoader = resourceLoader;
        this.dataSource = dataSource;
        this.fluentConfigurationCustomizers = fluentConfigurationCustomizers;
        this.javaMigrations = javaMigrations;
        this.callbacks = callbacks;
        this.resourceProviderCustomizer = resourceProviderCustomizer;
        this.migrationStrategy = migrationStrategy;
    }

    public Flyway createFlyway() {
        var configuration = new FlywayAutoConfiguration.FlywayConfiguration(null);
        return configuration.flyway(
                connectionDetails,
                resourceLoader,
                dataSource,
                EMPTY_PROVIDER,
                fluentConfigurationCustomizers,
                javaMigrations,
                callbacks,
                resourceProviderCustomizer
        );
    }

    public FlywayMigrationInitializer createFlywayInitializer(Flyway flyway) {
        var configuration = new FlywayAutoConfiguration.FlywayConfiguration(null);
        return configuration.flywayInitializer(flyway, migrationStrategy);
    }

    private static final ObjectProvider<DataSource> EMPTY_PROVIDER = new ObjectProvider<>() {
        @Nonnull
        @Override
        public DataSource getObject(@Nonnull Object... args) throws BeansException {
            throw new FatalBeanException("No value");
        }

        @Override
        public DataSource getIfAvailable() throws BeansException {
            return null;
        }

        @Override
        public DataSource getIfUnique() throws BeansException {
            return null;
        }

        @Nonnull
        @Override
        public DataSource getObject() throws BeansException {
            throw new FatalBeanException("No value");
        }
    };
}
