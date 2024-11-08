package orz.springboot.data.repo.primary;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import orz.springboot.data.repo.primary.model.PrimaryTest1Eo;

import java.util.List;

import static orz.springboot.data.repo.primary.model.QPrimaryTest1Eo.primaryTest1Eo;

@Repository
public class PrimaryRepo {
    private JPAQueryFactory queryFactory;

    @PersistenceContext(unitName = "primary")
    public void setEntityManager(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<PrimaryTest1Eo> queryV1() {
        return queryFactory
                .selectFrom(primaryTest1Eo)
                .fetch();
    }

    public PrimaryTest1Eo queryByIdV1(Long id) {
        return queryFactory
                .selectFrom(primaryTest1Eo)
                .where(primaryTest1Eo.id.eq(id))
                .fetchOne();
    }
}
