package orz.springboot.data.source;

import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;

public class OrzJdbcConnectionDetails implements JdbcConnectionDetails {
    private final String url;
    private final String username;
    private final String password;
    private final String driver;
    private final String xaDataSource;

    public OrzJdbcConnectionDetails(String url, String username, String password, String driver) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;
        // TODO: xaDataSource support
        this.xaDataSource = null;
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

    @Override
    public String getXaDataSourceClassName() {
        return xaDataSource;
    }
}
