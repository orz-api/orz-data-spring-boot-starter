package orz.springboot.data.source;

import org.springframework.boot.autoconfigure.flyway.FlywayConnectionDetails;

public class OrzFlywayConnectionDetails implements FlywayConnectionDetails {
    private final String url;
    private final String username;
    private final String password;
    private final String driver;

    public OrzFlywayConnectionDetails(String url, String username, String password, String driver) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;
    }

    @Override
    public String getJdbcUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getDriverClassName() {
        return driver;
    }
}
