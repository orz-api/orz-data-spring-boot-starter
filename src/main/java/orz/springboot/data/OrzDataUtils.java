package orz.springboot.data;

public class OrzDataUtils {
    public static String getJdbcDynamicConfigurationBeanName(String source) {
        return source + "JdbcDynamicConfiguration";
    }

    public static String getJdbcConnectionDetailsBeanName(String source) {
        return source + "JdbcConnectionDetails";
    }

    public static String getDataSourceBeanName(String source) {
        return source + "DataSource";
    }
}
