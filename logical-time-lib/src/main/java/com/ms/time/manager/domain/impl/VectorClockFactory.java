package com.ms.time.manager.domain.impl;

import com.ms.time.manager.domain.EventFactoryBuilder;
import com.ms.time.manager.dto.VectorClock;
import com.ms.time.manager.exception.LogicalTimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class VectorClockFactory implements EventFactoryBuilder {
    /**
     * Structure of registered services : [process-name, [process-number, clock]]
     * Example:
     * ["service-A", [0, [0]]
     * <p>
     * [
     * "service-A", [0, [0,0] ,
     * "service-B", [1, [0,0]
     * ]
     */
    private static Map<String, Map<String, Object>> registeredServices = new HashMap<>();
    private static final String PROCESS_NUMBER = "process-number";
    private static final String CLOCK = "clock";
    private static int serviceCount = 0;

    @Override
    public synchronized void registerService(String serviceName) {
        if (registeredServices.containsKey(serviceName)) {
            return;
        }

        Map<String, Object> processMap = new HashMap<>();
        registeredServices.put(serviceName, processMap);
        serviceCount++;

        var vectorClock = VectorClock.getInstance();
        List<Integer> initialVector = initializeNewServiceVector();
        vectorClock.setClock(initialVector);

        resizeOtherServices(registeredServices, serviceName,
                key -> {
                    var clockVector = (VectorClock) registeredServices.get(key).get(CLOCK);
                    clockVector.getClock().add(0);
                });

        processMap.put(PROCESS_NUMBER, serviceCount - 1);
        processMap.put(CLOCK, vectorClock);
    }

    @Override
    public void deRegisterService(String serviceName) {
        if(!registeredServices.containsKey(serviceName)) {
            return;
        }

        var processNumber = (Integer) registeredServices.get(serviceName).get(PROCESS_NUMBER);

        registeredServices.remove(serviceName);
        if (serviceCount > 0) {
            serviceCount--;
        }

        resizeOtherServices(registeredServices, serviceName,
                key -> {
                    var processMap = registeredServices.get(key);
                    var clockVector = ((VectorClock) processMap.get(CLOCK)).getClock();

                    List<Integer> newClock = new ArrayList<>(serviceCount);
                    for (int i = 0; i < clockVector.size(); i++) {
                        if (i != processNumber) newClock.add(clockVector.get(i));
                    }

                    ((VectorClock) processMap.get(CLOCK)).setClock(newClock);

                    // Decrease process-number after removal
                    if ((int) processMap.get(PROCESS_NUMBER) > processNumber) {
                        processMap.put(PROCESS_NUMBER, (int) processMap.get(PROCESS_NUMBER) - 1);
                    }
                });
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
        if (!registeredServices.containsKey(prevEventServiceName)) {
            throw new LogicalTimeException(String.format("Service %s is not registered", prevEventServiceName));
        }

        var service = registeredServices.get(serviceName);
        var processNumber = (Integer) service.get(PROCESS_NUMBER);
        var vectorClock = (VectorClock) service.get(CLOCK);


        if (StringUtils.isNotEmpty(prevEventServiceName) && registeredServices.containsKey(prevEventServiceName)) {
            var prevService = registeredServices.get(prevEventServiceName);
            var prevClockVector = ((VectorClock) prevService.get(CLOCK)).getClock();

            for (int val = 0; val < prevClockVector.size(); val++) {
                vectorClock.getClock().set(val,
                        Math.max(prevClockVector.get(val), vectorClock.getClock().get(val)));
            }
        }

        vectorClock.getClock().set(processNumber, vectorClock.getClock().get(processNumber) + 1);

        return vectorClock;
    }

    @Override
    public VectorClock getCurrentClock(String serviceName) {
        return (VectorClock) registeredServices.get(serviceName).get(CLOCK);
    }


    public Map<String, Map<String, Object>> getRegisteredService() {
        return registeredServices;
    }

    private List<Integer> initializeNewServiceVector() {
        List<Integer> vector = new ArrayList<>(serviceCount);
        for (int i = 0; i < serviceCount; i++) {
            vector.add(0);
        }
        return vector;
    }

    private void resizeOtherServices(Map<String, Map<String, Object>> registeredServices,
                                     String serviceName, Consumer<String> consumer) {
        registeredServices.keySet()
                .stream()
                .filter(k -> !serviceName.equalsIgnoreCase(k))
                .forEach(consumer::accept);
    }
}
