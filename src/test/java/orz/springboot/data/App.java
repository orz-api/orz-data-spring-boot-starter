package orz.springboot.data;

import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        RedissonAutoConfigurationV2.class,
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
