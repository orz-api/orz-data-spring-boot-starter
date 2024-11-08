package orz.springboot.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Validated
@ConfigurationProperties(prefix = OrzDataConstants.PROPS_PREFIX)
public class OrzDataProps {
    private static final SourceConfig SOURCE_DEFAULT = new SourceConfig();

    @Valid
    @NotNull
    private Map<String, SourceConfig> source = Collections.emptyMap();

    public SourceConfig getSource(String name) {
        return Optional.ofNullable(source.get(name)).orElse(SOURCE_DEFAULT);
    }

    @Data
    public static class SourceConfig {
        private String url;

        private String username;

        private String password;

        private String driver;

        @Valid
        @NotNull
        private SourceHikariConfig hikari = new SourceHikariConfig();

        @Valid
        @NotNull
        private SourceFlywayConfig flyway = new SourceFlywayConfig();

        @Valid
        @NotNull
        private SourceTransactionConfig transaction = new SourceTransactionConfig();

        @Valid
        @NotNull
        private SourceJdbcConfig jdbc = new SourceJdbcConfig();

        @Valid
        @NotNull
        private SourceJpaConfig jpa = new SourceJpaConfig();

        @Valid
        @NotNull
        private SourceMybatisConfig mybatis = new SourceMybatisConfig();
    }

    @Data
    public static class SourceHikariConfig {
    }

    @Data
    public static class SourceFlywayConfig {
        private Boolean enabled;

        private List<String> locations;
    }

    @Data
    public static class SourceTransactionConfig {
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration defaultTimeout;

        private Boolean rollbackOnCommitFailure;
    }

    @Data
    public static class SourceJdbcConfig {
        @Valid
        @NotNull
        private SourceJdbcTemplateConfig template = new SourceJdbcTemplateConfig();
    }

    @Data
    public static class SourceJdbcTemplateConfig {
    }

    @Data
    public static class SourceJpaConfig {
        @Valid
        @NotNull
        private SourceJpaHibernateConfig hibernate = new SourceJpaHibernateConfig();
    }

    @Data
    public static class SourceJpaHibernateConfig {
    }

    @Data
    public static class SourceMybatisConfig {
    }
}
