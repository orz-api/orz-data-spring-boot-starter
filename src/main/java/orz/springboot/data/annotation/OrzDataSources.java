package orz.springboot.data.annotation;

import org.springframework.context.annotation.Import;
import orz.springboot.data.OrzDataSourceRegistrar;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OrzDataSourceRegistrar.RepeatableRegistrar.class)
public @interface OrzDataSources {
    OrzDataSource[] value();
}
