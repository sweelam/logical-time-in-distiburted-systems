package com.ms.time.manager.domain.impl;

import com.ms.time.manager.dto.VectorClock;

import java.util.HashMap;
import java.util.Map;

public class VectorEventClockFactory {
    // [process-name, [process-number, clock]]
    private static Map<String, Map<String, Object>> registeredServices = new HashMap<>();

    public void registerService(String serviceName) {
        int processNumber = 0;
        if (registeredServices.containsKey(serviceName)) {
            var process = registeredServices.get(serviceName);
            processNumber = (Integer) process.get("process-number");
        }

        registeredServices.put(serviceName,
                Map.of("process-number", processNumber,
                "clock" , VectorClock.getInstance())

        );
    }

    public void deRegisterService(String serviceName) {
        registeredServices.remove(serviceName);
    }
}
