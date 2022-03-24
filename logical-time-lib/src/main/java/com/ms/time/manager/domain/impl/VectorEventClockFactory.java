package com.ms.time.manager.domain.impl;

import com.ms.time.manager.domain.EventFactoryBuilder;
import com.ms.time.manager.dto.VectorClock;
import com.ms.time.manager.exception.LogicalTimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VectorEventClockFactory implements EventFactoryBuilder {
    /**
     * Structure of registered services : [process-name, [process-number, clock]]
     * Example:
     * ["service-A", [0, [0]]
     *
     * [
     *      "service-A", [0, [0,0] ,
     *      "service-B", [1, [0,0]
     * ]
     * */
    private static Map<String, Map<String, Object>> registeredServices = new HashMap<>();

    private static final String PROCESS_NUMBER = "process-number";
    private static final String CLOCK = "clock";
    private static int serviceCount = 0;

    @Override
    public synchronized void registerService(String serviceName) {
        if (registeredServices.containsKey(serviceName))
            return;

        Map<String, Object> processMap = new HashMap<>();
        registeredServices.put(serviceName, processMap);
        serviceCount++;

        var vectorClock = VectorClock.getInstance();
        List<Integer> vector = new ArrayList<>(serviceCount);
        for (int i = 0; i < serviceCount; i++) {
            vector.add(0);
        }
        vectorClock.setClock(vector);

        processMap.put(PROCESS_NUMBER, serviceCount - 1);
        processMap.put(CLOCK, vectorClock);
    }

    @Override
    public void deRegisterService(String serviceName) {
        registeredServices.remove(serviceName);
    }


    @Override
    public VectorClock generateClockInstance(String serviceName) {
        if (!registeredServices.containsKey(serviceName)) {
            throw new LogicalTimeException("Service is not registered");
        }

        var service = registeredServices.get(serviceName);
        var processNumber = (Integer) service.get(PROCESS_NUMBER);
        var vectorClock = (VectorClock) service.get(CLOCK);
        vectorClock.getClock().set(processNumber, vectorClock.getClock().get(processNumber) + 1);

        return vectorClock;
    }

    @Override
    public VectorClock generateClockInstance(String serviceName, String prevEventServiceName) {
        return null;
    }

    @Override
    public VectorClock getCurrentClock(String serviceName) {
        return null;
    }


    public Map<String, Map<String, Object>> getRegisteredService() {
        return registeredServices;
    }

}
