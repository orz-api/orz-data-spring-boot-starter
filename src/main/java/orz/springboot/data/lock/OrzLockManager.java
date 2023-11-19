package orz.springboot.data.lock;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import static orz.springboot.base.OrzBaseUtils.check;

@Component
@ConditionalOnClass(RedissonClient.class)
public class OrzLockManager {
    private final RedissonClient redissonClient;
    private final String applicationName;

    public OrzLockManager(RedissonClient redissonClient, @Value("${spring.application.name}") String applicationName) {
        check(StringUtils.isNotBlank(applicationName), "StringUtils.isNotBlank(applicationName)");
        this.redissonClient = redissonClient;
        this.applicationName = applicationName;
    }

    public OrzLock getLock(OrzLockName name) {
        return new OrzLock(redissonClient.getLock(name.getName(applicationName)));
    }

    public OrzLockExecutor executor(OrzLockName name) {
        return new OrzLockExecutor(getLock(name));
    }
}
