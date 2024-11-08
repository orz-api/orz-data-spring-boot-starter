package orz.springboot.data.source;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.boot.autoconfigure.OrzMybatisRegistrar;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.flyway.OrzFlywayRegistrar;
import org.springframework.boot.autoconfigure.jdbc.OrzJdbcRegistrar;
import org.springframework.boot.autoconfigure.orm.jpa.OrzJpaRegistrar;
import org.springframework.boot.autoconfigure.transaction.OrzTransactionRegistrar;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import orz.springboot.data.OrzDataConstants;
import orz.springboot.data.OrzDataProps;
import orz.springboot.data.source.annotation.OrzDataSource;
import orz.springboot.data.source.annotation.OrzDataSources;

import java.util.Objects;

@Slf4j
@Import(OrzDataSourceBeanDefinitionRegistryPostProcessor.class)
public class OrzDataSourceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanFactoryAware {
    private Environment environment;
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanFactory(@Nonnull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata metadata, @Nonnull BeanDefinitionRegistry registry) {
        var attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(OrzDataSource.class.getName()));
        registerBeanDefinitions(metadata, Objects.requireNonNull(attributes), registry);
    }

    protected void registerBeanDefinitions(AnnotationMetadata metadata, AnnotationAttributes attributes, BeanDefinitionRegistry registry) {
        if (!beanFactory.containsSingleton(OrzDataSourceAnnotations.class.getName())) {
            beanFactory.registerSingleton(OrzDataSourceAnnotations.class.getName(), new OrzDataSourceAnnotations());
        }

        var name = attributes.getString("name");
        var props = new OrzDataProps();
        if (!Binder.get(environment).bind(OrzDataConstants.PROPS_PREFIX, Bindable.ofInstance(props)).isBound()) {
            log.debug("OrzDataProps not bound");
        }
        var config = props.getSource(name);

        var annotations = beanFactory.getBean(OrzDataSourceAnnotations.class);
        var annotation = new OrzDataSourceAnnotation(name, metadata, attributes, config);
        annotations.register(annotation);

        OrzJdbcRegistrar.registerBeanDefinitions(environment, registry, annotation);
        OrzFlywayRegistrar.registerBeanDefinitions(environment, registry, annotation);
        OrzJpaRegistrar.registerBeanDefinitions(environment, registry, annotation);
        OrzMybatisRegistrar.registerBeanDefinitions(environment, registry, annotation);
        OrzJdbcRegistrar.registerTransactionBeanDefinitions(environment, registry, annotation);
        OrzTransactionRegistrar.registerBeanDefinitions(environment, registry, annotation);
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
