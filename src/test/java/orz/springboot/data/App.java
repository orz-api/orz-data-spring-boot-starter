package orz.springboot.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import orz.springboot.data.annotation.OrzDataSource;

@SpringBootApplication
@OrzDataSource(name = "primary")
@OrzDataSource(name = "secondary")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
