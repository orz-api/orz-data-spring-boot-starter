package orz.springboot.data.source.annotation;

import org.springframework.context.annotation.Import;
import orz.springboot.data.source.OrzDataSourceRegistrar;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OrzDataSourceRegistrar.RepeatableRegistrar.class)
public @interface OrzDataSources {
    OrzDataSource[] value();
}
