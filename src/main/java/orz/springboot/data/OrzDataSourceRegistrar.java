package orz.springboot.data;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.jdbc.OrzJdbcDynamicConfiguration;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import orz.springboot.data.annotation.OrzDataSource;
import orz.springboot.data.annotation.OrzDataSources;

import static orz.springboot.base.description.OrzDescriptionUtils.desc;

@Slf4j
public class OrzDataSourceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private Environment environment;

    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata metadata, @Nonnull BeanDefinitionRegistry registry) {
        registerBeanDefinitions(metadata, AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(OrzDataSource.class.getName())), registry);
    }

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = environment;
    }

    protected void registerBeanDefinitions(AnnotationMetadata metadata, AnnotationAttributes attributes, BeanDefinitionRegistry registry) {
        var propsBindResult = Binder.get(environment).bind(OrzDataConstants.PROPS_PREFIX, OrzDataProps.class);
        if (propsBindResult.isBound()) {
            System.out.println("=====================================OrzDataSourceRegistrar: " + propsBindResult.get());
            System.out.println(metadata);
            System.out.println(attributes);
            var name = attributes.getString("name");

            // TODO
            OrzJdbcDynamicConfiguration.registerBeanDefinitions1(registry, name, propsBindResult.get().getSource().get(name));
        } else {
            log.error(desc("OrzDataProps not bound", "prefix", "orz.data"));
        }
    }

    public static class RepeatableRegistrar extends OrzDataSourceRegistrar {
        @Override
        public void registerBeanDefinitions(@Nonnull AnnotationMetadata metadata, @Nonnull BeanDefinitionRegistry registry) {
            var dataSourceAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(OrzDataSources.class.getName()));
            if (dataSourceAttributes != null) {
                var annotations = dataSourceAttributes.getAnnotationArray("value");
                for (var annotation : annotations) {
                    registerBeanDefinitions(metadata, annotation, registry);
                }
            }
        }
    }
}
