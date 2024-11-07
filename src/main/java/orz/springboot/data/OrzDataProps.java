package orz.springboot.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.Map;

@Data
@Validated
@ConfigurationProperties(prefix = "orz.data")
public class OrzDataProps {
    @Valid
    @NotNull
    private final Map<String, SourceConfig> source = Collections.emptyMap();

    @Data
    public static class SourceConfig {
        @NotBlank
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
        private SourceJdbcTemplateConfig jdbcTemplate = new SourceJdbcTemplateConfig();

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
    }

    @Data
    public static class SourceTransactionConfig {
    }

    @Data
    public static class SourceJdbcTemplateConfig {
    }

    @Data
    public static class SourceJpaConfig {
    }

    @Data
    public static class SourceMybatisConfig {
    }
}
