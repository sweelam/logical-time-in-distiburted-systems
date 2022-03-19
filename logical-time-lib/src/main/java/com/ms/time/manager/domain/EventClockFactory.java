package com.ms.time.manager.domain;

import com.ms.time.manager.dto.EventClock;
import com.ms.time.manager.exception.LogicalTimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EventClockFactory {
    private static Map<String, EventClock> registeredServices = new HashMap<>();

    public EventClock getEventClockInstance(String serviceName) {
        if(!registeredServices.containsKey(serviceName)) {
            throw new LogicalTimeException("Service is not registered");
        }

        EventClock result = registeredServices.get(serviceName);
        result.setClock(TimeUnit.SECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS) + 1);
        registeredServices.put(serviceName, result);

        return result;
    }

    public EventClock getEventClockInstance(String serviceName, String prevEventServiceName) {
        if(!registeredServices.containsKey(serviceName)) {
            throw new LogicalTimeException("Service is not registered");
        }

        EventClock result = registeredServices.get(serviceName);
        long maxClock = TimeUnit.SECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);

        if (StringUtils.isNotEmpty(prevEventServiceName) && registeredServices.containsKey(prevEventServiceName)) {
            var senderClock = registeredServices.get(prevEventServiceName);
            maxClock = Math.max(senderClock.getClock(), maxClock);
        }

        result.setClock((maxClock % 12) + 1);
        registeredServices.put(serviceName, result);

        return result;
    }

    public void registerService(String serviceName) {
        registeredServices.put(serviceName, EventClock.getInstance());
    }

    public EventClock getCurrentClock(String serviceName) {
        return registeredServices.get(serviceName);
    }
}
