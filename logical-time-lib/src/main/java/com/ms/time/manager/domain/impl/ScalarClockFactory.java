package com.ms.time.manager.domain.impl;

import com.ms.time.manager.domain.EventFactoryBuilder;
import com.ms.time.manager.dto.ScalarClock;
import com.ms.time.manager.exception.LogicalTimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ScalarClockFactory implements EventFactoryBuilder {
    private static Map<String, ScalarClock> registeredServices = new HashMap<>();

    @Override
    public void registerService(String serviceName) {
        registeredServices.computeIfAbsent(serviceName, k -> ScalarClock.getInstance());
    }

    @Override
    public void deRegisterService(String serviceName) {
        registeredServices.remove(serviceName);
    }

    @Override
    public ScalarClock generateClockInstance(String serviceName) {
        if (!registeredServices.containsKey(serviceName)) {
            throw new LogicalTimeException("Service is not registered");
        }

        ScalarClock result = registeredServices.get(serviceName);
        result.setClock(result.getClock() + 1);
        registeredServices.put(serviceName, result);

        return result;
    }

    @Override
    public ScalarClock generateClockInstance(String serviceName, String prevEventServiceName) {
        if (!registeredServices.containsKey(serviceName)) {
            throw new LogicalTimeException("Service is not registered");
        }

        ScalarClock result = registeredServices.get(serviceName);
        int maxClock = result.getClock();

        if (StringUtils.isNotEmpty(prevEventServiceName) && registeredServices.containsKey(prevEventServiceName)) {
            var senderClock = registeredServices.get(prevEventServiceName);
            maxClock = Math.max(senderClock.getClock(), maxClock);
        }

        result.setClock(maxClock + 1);
        registeredServices.put(serviceName, result);

        return result;
    }

    @Override
    public ScalarClock getCurrentClock(String serviceName) {
        return registeredServices.get(serviceName);
    }

    public Map<String, ScalarClock> getRegisteredServices() {
        return registeredServices;
    }
}
