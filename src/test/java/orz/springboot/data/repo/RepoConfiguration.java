package orz.springboot.data.repo;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import orz.springboot.data.annotation.OrzDataSource;
import orz.springboot.data.repo.primary.model.PrimaryTest1Eo;
import orz.springboot.data.repo.secondary.model.SecondaryTest1Eo;

@OrzDataSource(name = "primary", jpa = true, mybatis = true, entityClasses = PrimaryTest1Eo.class)
@OrzDataSource(name = "secondary", jpa = true, entityClasses = SecondaryTest1Eo.class)
@Configuration
public class RepoConfiguration {
    @Configuration
    @EnableJpaRepositories(
            basePackages = "orz.springboot.data.repo.primary",
            entityManagerFactoryRef = "primaryEntityManagerFactory",
            transactionManagerRef = "primaryTransactionManager"
    )
    public static class PrimaryJpaConfiguration {
    }

    @Configuration
    @EnableJpaRepositories(
            basePackages = "orz.springboot.data.repo.secondary",
            entityManagerFactoryRef = "secondaryEntityManagerFactory",
            transactionManagerRef = "secondaryTransactionManager"
    )
    public static class SecondaryJpaConfiguration {
    }

    @Configuration
    @MapperScan(
            basePackages = "orz.springboot.data.repo.primary",
            sqlSessionFactoryRef = "primarySqlSessionFactory",
            sqlSessionTemplateRef = "primarySqlSessionTemplate",
            annotationClass = Mapper.class
    )
    public static class PrimaryMybatisConfiguration {
    }
}
