package orz.springboot.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import orz.springboot.data.repo.primary.PrimaryJdbc;
import orz.springboot.data.repo.primary.PrimaryRepo;
import orz.springboot.data.repo.primary.PrimaryTest1Jpa;
import orz.springboot.data.repo.primary.PrimaryTest1Mapper;
import orz.springboot.data.repo.primary.model.PrimaryTest1Eo;
import orz.springboot.data.repo.secondary.SecondaryJdbc;

@SpringBootTest
class AppTests {
    @Autowired
    PrimaryTest1Jpa primaryTest1Jpa;

    @Autowired
    PrimaryJdbc primaryJdbc;

    @Autowired
    PrimaryTest1Mapper primaryTest1Mapper;

    @Autowired
    orz.springboot.data.repo.primary.TestJpa primaryTestJpa;

    @Autowired
    orz.springboot.data.repo.secondary.TestJpa secondaryTestJpa;

    @Autowired
    private PrimaryRepo primaryRepo;

    @Autowired
    private SecondaryJdbc secondaryJdbc;

    @Test
    void contextLoads() {
    }

    @Test
    void testV5() {
        secondaryJdbc.queryV1();

        OrzTransaction.of("primary").propagationRequiresNew().exec(() -> {
            primaryTest1Jpa.saveAndFlush(new PrimaryTest1Eo(null, "from testV5 1"));
            OrzLock.of("testLockV1", true).transaction("primary").propagationRequiresNew().exec(() -> {
                System.out.println("========================== query1:" + primaryJdbc.queryV1());
                primaryTest1Jpa.saveAndFlush(new PrimaryTest1Eo(null, "from testV5 2"));
            });
        });
        try {
            OrzTransaction.of("primary").exec(() -> {
                primaryTest1Mapper.insertTestV1(new PrimaryTest1Eo(null, "from testV5 3"));
                System.out.println("========================== query2:" + primaryTest1Mapper.queryTestV1());
                throw new RuntimeException("transaction failed");
            });
        } catch (Exception e) {
            System.out.println("========================== transaction failed");
        }
        System.out.println("========================== query3:" + primaryJdbc.queryV1());
        System.out.println("========================== query4:" + primaryTest1Jpa.findAll());
        System.out.println("========================== query5:" + primaryTest1Mapper.queryTestV1());
        System.out.println("========================== query6:" + primaryRepo.queryV1());
    }

    @Test
    void test6() {
        primaryJdbc.queryV1();
        primaryRepo.queryV1();
    }

    @Test
    void test7() {
        primaryTestJpa.saveAndFlush(new orz.springboot.data.repo.primary.model.TestEo("中文", "from test7"));
        secondaryTestJpa.saveAndFlush(new orz.springboot.data.repo.secondary.model.TestEo("中文", "from test7"));
    }
}
