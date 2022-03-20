package com.ms.time.manager.domain;

import com.ms.time.manager.dto.EventClock;
import com.ms.time.manager.exception.LogicalTimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ScalarEventClockFactory {
    private static Map<String, EventClock> registeredServices = new HashMap<>();

    public void registerService(String serviceName) {
        registeredServices.put(serviceName, EventClock.getInstance());
    }

    public void deRegisterService(String serviceName) {
        registeredServices.remove(serviceName);
    }

    public EventClock generateClockInstance(String serviceName) {
        if (!registeredServices.containsKey(serviceName)) {
            throw new LogicalTimeException("Service is not registered");
        }

        EventClock result = registeredServices.get(serviceName);
        result.setClock(result.getClock() + 1);
        registeredServices.put(serviceName, result);

        return result;
    }

    public EventClock generateClockInstance(String serviceName, String prevEventServiceName) {
        if (!registeredServices.containsKey(serviceName)) {
            throw new LogicalTimeException("Service is not registered");
        }

        EventClock result = registeredServices.get(serviceName);
        long maxClock = result.getClock();

        if (StringUtils.isNotEmpty(prevEventServiceName) && registeredServices.containsKey(prevEventServiceName)) {
            var senderClock = registeredServices.get(prevEventServiceName);
            maxClock = Math.max(senderClock.getClock(), maxClock);
        }

        result.setClock(maxClock + 1);
        registeredServices.put(serviceName, result);

        return result;
    }

    public EventClock getCurrentClock(String serviceName) {
        return registeredServices.get(serviceName);
    }
}
