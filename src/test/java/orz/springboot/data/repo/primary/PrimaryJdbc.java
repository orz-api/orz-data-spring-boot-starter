package orz.springboot.data.repo.primary;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class PrimaryJdbc {
    private final JdbcTemplate primaryJdbcTemplate;

    public PrimaryJdbc(JdbcTemplate primaryJdbcTemplate) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
    }

    public List<Map<String, Object>> showTablesV1() {
        return primaryJdbcTemplate.queryForList("SHOW TABLES");
    }

    @Transactional(transactionManager = "primaryTransactionManager")
    public void testTransactionV1() {
        primaryJdbcTemplate.update("INSERT INTO primary_test_1 (f1) VALUES ('v1')");
        throw new RuntimeException();
    }

    @Transactional(transactionManager = "primaryTransactionManager")
    public void testTransactionV2() {
        primaryJdbcTemplate.update("INSERT INTO primary_test_1 (f1) VALUES ('v2')");
    }

    @Transactional(transactionManager = "primaryTransactionManager")
    public List<Map<String, Object>> queryV1() {
        return primaryJdbcTemplate.queryForList("SELECT * FROM primary_test_1");
    }
}
