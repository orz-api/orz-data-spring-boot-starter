package orz.springboot.data.repo.secondary;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class SecondaryJdbc {
    private final JdbcTemplate secondaryJdbcTemplate;
//    private final NamedParameterJdbcTemplate secondaryNamedParameterJdbcTemplate;

//    public SecondaryJdbc(JdbcTemplate secondaryJdbcTemplate, NamedParameterJdbcTemplate secondaryNamedParameterJdbcTemplate) {
//        this.secondaryJdbcTemplate = secondaryJdbcTemplate;
//        this.secondaryNamedParameterJdbcTemplate = secondaryNamedParameterJdbcTemplate;
//    }

    public SecondaryJdbc(JdbcTemplate secondaryJdbcTemplate) {
        this.secondaryJdbcTemplate = secondaryJdbcTemplate;
    }

    public List<Map<String, Object>> showTablesV1() {
        return secondaryJdbcTemplate.queryForList("SHOW TABLES");
    }

    public List<Map<String, Object>> showTablesV2() {
//        return secondaryNamedParameterJdbcTemplate.queryForList("SHOW TABLES", Map.of());
        return List.of();
    }

    @Transactional(transactionManager = "secondaryTransactionManager")
    public List<Map<String, Object>> queryV1() {
        return secondaryJdbcTemplate.queryForList("SELECT * FROM secondary_test_1");
    }
}
