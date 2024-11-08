package orz.springboot.data.repo.primary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orz.springboot.data.repo.primary.model.PrimaryTest1Eo;

@Repository
public interface PrimaryTest1Jpa extends JpaRepository<PrimaryTest1Eo, Long> {
}
