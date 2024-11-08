package orz.springboot.data;

import com.redis.testcontainers.RedisContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import orz.springboot.data.source.OrzJdbcConnectionDetails;

import javax.annotation.Nonnull;

@Slf4j
@Configuration
public class TestConfiguration implements TestExecutionListener, Ordered {
    @ConditionalOnProperty("test.enable-test-container")
    @Bean
    public MySQLContainer<?> primaryMysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:9.0.1"));
    }

    @ConditionalOnProperty("test.enable-test-container")
    @Bean
    public MySQLContainer<?> secondaryMysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:9.0.1"));
    }

    @ConditionalOnProperty("test.enable-test-container")
    @Bean
    @ServiceConnection("redis")
    public RedisContainer redisContainer() {
        return new RedisContainer(DockerImageName.parse("redis:7.4.0"));
    }

    @ConditionalOnProperty("test.enable-test-container")
    @Bean
    public OrzJdbcConnectionDetails primaryJdbcConnectionDetails(MySQLContainer<?> primaryMysqlContainer) {
        return new OrzJdbcConnectionDetails(
                primaryMysqlContainer.getJdbcUrl(),
                primaryMysqlContainer.getUsername(),
                primaryMysqlContainer.getPassword(),
                primaryMysqlContainer.getDriverClassName()
        );
    }

    @ConditionalOnProperty("test.enable-test-container")
    @Bean
    public OrzJdbcConnectionDetails secondaryJdbcConnectionDetails(MySQLContainer<?> secondaryMysqlContainer) {
        return new OrzJdbcConnectionDetails(
                secondaryMysqlContainer.getJdbcUrl(),
                secondaryMysqlContainer.getUsername(),
                secondaryMysqlContainer.getPassword(),
                secondaryMysqlContainer.getDriverClassName()
        );
    }

    @Override
    public void beforeTestClass(@Nonnull TestContext testContext) throws Exception {
        System.setProperty("spring.profiles.active", "test");
        TestExecutionListener.super.beforeTestClass(testContext);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
