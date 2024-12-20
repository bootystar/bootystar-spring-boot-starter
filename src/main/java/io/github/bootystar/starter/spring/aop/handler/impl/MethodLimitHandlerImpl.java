package io.github.bootystar.starter.spring.aop.handler.impl;

import io.github.bootystar.starter.spring.aop.exception.MethodLimitException;
import io.github.bootystar.starter.spring.aop.handler.MethodLimitHandler;
import lombok.SneakyThrows;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author bootystar
 */
public class MethodLimitHandlerImpl implements MethodLimitHandler {
    private final ConcurrentHashMap<String, Lock> LOCK_MAP = new ConcurrentHashMap<>();

    @Override
    @SneakyThrows
    public boolean tryLock(int timeout, String signature) {
        Lock lock = getLock(signature);
        if (timeout <= 0) {
            return lock.tryLock();
        }
        return lock.tryLock(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void unLock(String signature) {
        getLock(signature).unlock();
        LOCK_MAP.remove(signature);
    }


    private Lock getLock(String signature) {
        Lock lock = LOCK_MAP.get(signature);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        LOCK_MAP.putIfAbsent(signature, lock);
        return LOCK_MAP.get(signature);
    }


}
