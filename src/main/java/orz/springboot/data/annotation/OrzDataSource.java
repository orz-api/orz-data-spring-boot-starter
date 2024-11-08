package orz.springboot.data.annotation;

import org.springframework.context.annotation.Import;
import orz.springboot.data.source.OrzDataSourceRegistrar;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(OrzDataSources.class)
@Import(OrzDataSourceRegistrar.class)
public @interface OrzDataSource {
    String name();

    boolean jpa() default false;

    boolean mybatis() default false;

    Class<?>[] entityClasses() default {};

    String[] entityPackages() default {};
}
