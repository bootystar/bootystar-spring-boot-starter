package io.github.bootystar.autoconfigure.core;

import io.github.bootystar.autoconfigure.prop.AopProperties;
import io.github.bootystar.autoconfigure.spring.aspect.MethodLimitAspect;
import io.github.bootystar.autoconfigure.spring.handler.MethodLimitHandler;
import io.github.bootystar.autoconfigure.spring.handler.impl.RedissonMethodLimitHandler;
import io.github.bootystar.autoconfigure.spring.handler.impl.ReentrantLockMethodLimitHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.Advice;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

/**
 * bootystar aop自动配置
 *
 * @author bootystar
 * @see org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
 */
@Slf4j
@AutoConfiguration(after = {AopAutoConfiguration.class})
@ConditionalOnClass({Advice.class})
@EnableConfigurationProperties(AopProperties.class)
@ConditionalOnProperty(prefix = "bootystar.aop", name = "enabled", havingValue = "true", matchIfMissing = true)
public class BootystarAopAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 方法限流切面
     *
     * @return {@link MethodLimitAspect }
     * @author bootystar
     * @see org.redisson.spring.starter.RedissonAutoConfiguration
     */
    @Bean
    public MethodLimitAspect methodLimitAspect() {
        MethodLimitAspect methodLimitAspect = new MethodLimitAspect();
        try {
            Class<?> clazz = Class.forName("org.redisson.api.RedissonClient");
            Object bean = applicationContext.getBean(clazz);
            RedissonMethodLimitHandler redissonHandler = new RedissonMethodLimitHandler((RedissonClient) bean);
            methodLimitAspect.allocateLimitHandler(MethodLimitHandler.class, redissonHandler);
            log.debug("MethodLimitHandlerRedissonImpl Configured");
        } catch (Exception e) {
            methodLimitAspect.allocateLimitHandler(MethodLimitHandler.class, new ReentrantLockMethodLimitHandler());
            log.debug("MethodLimitHandlerReentrantLockImpl Configured");
        }
        return methodLimitAspect;
    }


}