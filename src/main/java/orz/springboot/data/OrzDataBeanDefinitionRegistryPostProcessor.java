package orz.springboot.data;

import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.OrzJdbcDynamicConfiguration;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OrzDataBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@Nonnull BeanDefinitionRegistry registry) throws BeansException {
        var environment = applicationContext.getEnvironment();
        var propsBindResult = Binder.get(environment).bind(OrzDataConstants.PROPS_PREFIX, OrzDataProps.class);
        if (propsBindResult.isBound()) {
            var props = propsBindResult.get();
            props.getSource().forEach((source, config) -> {
                OrzJdbcDynamicConfiguration.registerBeanDefinitions2(registry, source, config);
            });
        }
    }
}
