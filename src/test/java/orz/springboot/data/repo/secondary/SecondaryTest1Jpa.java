package orz.springboot.data.repo.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import orz.springboot.data.repo.secondary.model.SecondaryTest1Eo;

@Repository
public interface SecondaryTest1Jpa extends JpaRepository<SecondaryTest1Eo, Long> {
}
