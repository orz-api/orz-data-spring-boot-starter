package orz.springboot.data.repo.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orz.springboot.data.repo.secondary.model.TestEo;

@Repository("secondaryTestJpa")
public interface TestJpa extends JpaRepository<TestEo, String> {
}
