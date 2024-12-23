package io.github.bootystar.starter.autoconfigure;

import io.github.bootystar.starter.spring.aop.aspect.MethodLimitAspect;
import io.github.bootystar.starter.spring.aop.handler.MethodLimitHandler;
import io.github.bootystar.starter.spring.aop.handler.impl.MethodLimitHandlerRedissonImpl;
import io.github.bootystar.starter.spring.aop.handler.impl.MethodLimitHandlerReentrantLockImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.Advice;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * bootystar aop自动配置
 * @see org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
 * @author bootystar
 */
@Slf4j
@ConditionalOnClass({Advice.class})
@AutoConfiguration(after = {AopAutoConfiguration.class})
public class BootystarAopAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 方法限流切面
     *
     * @see org.redisson.spring.starter.RedissonAutoConfiguration
     * @return {@link MethodLimitAspect }
     * @author bootystar
     */
    @Bean
    public MethodLimitAspect methodLimitAspect() {
        MethodLimitAspect methodLimitAspect = new MethodLimitAspect();
        try {
            Class<?> clazz = Class.forName("org.redisson.api.RedissonClient");
            Object bean = applicationContext.getBean(clazz);
            MethodLimitHandlerRedissonImpl redissonHandler = new MethodLimitHandlerRedissonImpl((RedissonClient) bean);
            methodLimitAspect.allocateLimitHandler(MethodLimitHandler.class, redissonHandler);
            log.debug("MethodLimitHandlerRedissonImpl Configured");
        } catch (Exception e) {
            methodLimitAspect.allocateLimitHandler(MethodLimitHandler.class, new MethodLimitHandlerReentrantLockImpl());
            log.debug("MethodLimitHandlerReentrantLockImpl Configured");
        }
        return methodLimitAspect;
    }


}
