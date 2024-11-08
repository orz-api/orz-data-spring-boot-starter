package orz.springboot.data.repo.primary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orz.springboot.data.repo.primary.model.TestEo;

@Repository("primaryTestJpa")
public interface TestJpa extends JpaRepository<TestEo, String> {
}
